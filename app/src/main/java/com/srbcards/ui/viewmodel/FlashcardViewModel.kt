package com.srbcards.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.srbcards.data.local.VocabularyWord
import com.srbcards.data.local.WordDatabase
import com.srbcards.data.repository.WordRepository
import com.srbcards.domain.AppMode
import com.srbcards.domain.CardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the entire flashcard session. It is scoped to the Activity so both
 * [MainScreen] and [CardScreen] share the same instance and session state.
 *
 * Distractor generation strategy:
 *  - SR_TO_RU / RU_TO_SR  → distractors drawn from the [wordRu] / [wordSrSing] columns
 *    of other words with the **same type** as the question word.
 *  - SING_TO_PLUR          → distractors drawn from the [wordSrPlur] column of other nouns
 *    that also have a valid (non-N/A) plural form.
 *  - Fallback: if same-type pool is too small, [WordRepository.getDistractors] automatically
 *    pads with type-agnostic words to guarantee 4 options.
 */
class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository by lazy {
        WordRepository(WordDatabase.getDatabase(application).vocabularyDao())
    }

    private val _uiState = MutableStateFlow(CardUiState())
    val uiState: StateFlow<CardUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    init {
        loadCategories()
        loadNextCard()
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /** Switch to a new [AppMode] and reset the session score. */
    fun setMode(mode: AppMode) {
        _uiState.update { it.copy(currentMode = mode, score = 0, total = 0) }
        loadNextCard()
    }

    /** Apply a category filter. Pass null to remove the filter. */
    fun setCategory(category: String?) {
        _uiState.update { it.copy(currentCategory = category) }
    }

    /** Called when the user taps an answer option. Idempotent after first call. */
    fun submitAnswer(selected: String) {
        val state = _uiState.value
        if (state.selectedOption != null) return // already answered

        val correct = selected.trim().equals(state.correctAnswer.trim(), ignoreCase = true)
        _uiState.update {
            it.copy(
                selectedOption = selected,
                isCorrect = correct,
                score = if (correct) it.score + 1 else it.score,
                total = it.total + 1
            )
        }
    }

    /** Loads the next card. Called on init and after each "Next" button press. */
    fun loadNextCard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedOption = null, isCorrect = null, error = null) }
            try {
                val state = _uiState.value
                val mode = state.currentMode
                val category = state.currentCategory

                val pool = fetchPool(mode, category)

                if (pool.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Слова не найдены. Попробуйте выбрать другую категорию."
                        )
                    }
                    return@launch
                }

                val question = pool.random()
                val (prompt, correctAnswer) = extractPromptAndAnswer(question, mode)
                val options = buildOptions(question, mode, correctAnswer)
                val genderTag = question.gender.takeIf { question.type == "noun" && it != "N/A" }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        prompt = prompt,
                        genderTag = genderTag,
                        category = question.category,
                        correctAnswer = correctAnswer,
                        options = options,
                        selectedOption = null,
                        isCorrect = null,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Ошибка загрузки: ${e.message}")
                }
            }
        }
    }

    /** Resets session score while keeping the current mode and category. */
    fun resetSession() {
        _uiState.update { it.copy(score = 0, total = 0) }
        loadNextCard()
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun loadCategories() {
        viewModelScope.launch {
            runCatching { repository.getAllCategories() }
                .onSuccess { _categories.value = it }
        }
    }

    private suspend fun fetchPool(mode: AppMode, category: String?): List<VocabularyWord> =
        when {
            mode == AppMode.SING_TO_PLUR && category != null ->
                repository.getRandomNounsWithPluralByCategory(category, 20)
            mode == AppMode.SING_TO_PLUR ->
                repository.getRandomNounsWithPlural(20)
            category != null ->
                repository.getRandomByCategory(category, 20)
            else ->
                repository.getRandomWords(20)
        }

    private fun extractPromptAndAnswer(
        word: VocabularyWord,
        mode: AppMode
    ): Pair<String, String> = when (mode) {
        AppMode.SR_TO_RU  -> word.wordSrSing to word.wordRu
        AppMode.RU_TO_SR  -> word.wordRu      to word.wordSrSing
        AppMode.SING_TO_PLUR -> word.wordSrSing to word.wordSrPlur
    }

    private suspend fun buildOptions(
        question: VocabularyWord,
        mode: AppMode,
        correctAnswer: String
    ): List<String> {
        val distractorWords = if (mode == AppMode.SING_TO_PLUR) {
            repository.getDistractorsWithPlural(question.id, question.type, 3)
        } else {
            repository.getDistractors(question.id, question.type, 3)
        }

        val distractorAnswers = distractorWords.map { word ->
            when (mode) {
                AppMode.SR_TO_RU    -> word.wordRu
                AppMode.RU_TO_SR    -> word.wordSrSing
                AppMode.SING_TO_PLUR -> word.wordSrPlur
            }
        }

        // Merge, deduplicate, ensure correct answer is included, then shuffle
        return (distractorAnswers + correctAnswer)
            .distinct()
            .take(4)
            .shuffled()
    }
}
