package com.srbcards.domain

/**
 * Immutable snapshot of the flashcard screen's UI state, exposed as a [StateFlow].
 *
 * State machine transitions:
 *  1. [isLoading] = true   → spinner shown, all other fields are defaults
 *  2. [isLoading] = false  → card renders [prompt], [options], gender badge
 *  3. User taps an option  → [selectedOption] is set, [isCorrect] is evaluated
 *  4. User taps "Next"     → ViewModel resets to state 1 with updated [score]/[total]
 */
data class CardUiState(
    /** True while a new card is being fetched from the database. */
    val isLoading: Boolean = true,

    /** The word or phrase displayed as the question. */
    val prompt: String = "",

    /**
     * Grammatical gender of the question word. Only non-null when the word
     * is a noun and has a known gender ("m", "f", or "n").
     */
    val genderTag: String? = null,

    /** Category label displayed as a subtitle under the prompt (e.g. "Food & Drinks"). */
    val category: String = "",

    /** The expected correct answer for the current card. */
    val correctAnswer: String = "",

    /**
     * Shuffled list of 4 answer strings: 1 correct + 3 distractors.
     * May contain fewer than 4 if the dataset is too small for a given filter combination.
     */
    val options: List<String> = emptyList(),

    /** The option the user tapped. Null while the question is still unanswered. */
    val selectedOption: String? = null,

    /** Null = unanswered; true = correct; false = incorrect. */
    val isCorrect: Boolean? = null,

    /** Number of correct answers in the current session. */
    val score: Int = 0,

    /** Total number of questions answered in the current session. */
    val total: Int = 0,

    /** Active flashcard mode. */
    val currentMode: AppMode = AppMode.SR_TO_RU,

    /** Active category filter. Null means "all categories". */
    val currentCategory: String? = null,

    /** Non-null when a recoverable error occurred (e.g. empty query result). */
    val error: String? = null
)
