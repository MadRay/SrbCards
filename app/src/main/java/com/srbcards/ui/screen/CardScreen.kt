package com.srbcards.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.srbcards.domain.AppMode
import com.srbcards.domain.CardUiState
import com.srbcards.ui.theme.*

// ── Gender badge helpers ──────────────────────────────────────────────────────

private data class GenderStyle(val label: String, val color: Color, val container: Color)

private fun genderStyle(tag: String?): GenderStyle? = when (tag) {
    "m"  -> GenderStyle("муж.", GenderMale, GenderMaleContainer)
    "f"  -> GenderStyle("жен.", GenderFemale, GenderFemaleContainer)
    "n"  -> GenderStyle("ср.",  GenderNeuter, GenderNeuterContainer)
    else -> null
}

// ── Root composable ───────────────────────────────────────────────────────────

/**
 * The flashcard gameplay screen.
 *
 * Layout (top → bottom):
 *  1. [TopAppBar] — mode label + session score
 *  2. [QuestionCard] — prompt word, gender badge, category label (animates on card change)
 *  3. [AnswerGrid]  — 2 × 2 button grid with animated colour feedback
 *  4. "Next Word" [FilledTonalButton] — appears after an answer is submitted
 *
 * @param uiState   Observed flashcard state from [FlashcardViewModel].
 * @param onAnswer  Callback with the tapped option string.
 * @param onNext    Callback when the "Next" button is tapped.
 * @param onBack    Callback to navigate back to the main screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(
    uiState: CardUiState,
    onAnswer: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CardTopBar(
                mode = uiState.currentMode,
                score = uiState.score,
                total = uiState.total,
                onBack = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    ErrorCard(message = uiState.error, onRetry = onNext)
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Animated card — slides in from the right on each new word
                        AnimatedContent(
                            targetState = uiState.prompt,
                            transitionSpec = {
                                (slideInHorizontally { it } + fadeIn()) togetherWith
                                        (slideOutHorizontally { -it } + fadeOut())
                            },
                            label = "card_transition",
                            modifier = Modifier.weight(1f)
                        ) { _ ->
                            QuestionCard(uiState = uiState)
                        }

                        // Answer grid
                        AnswerGrid(
                            options = uiState.options,
                            correctAnswer = uiState.correctAnswer,
                            selectedOption = uiState.selectedOption,
                            onSelect = onAnswer
                        )

                        // Next button — only shown after the user has answered
                        AnimatedVisibility(
                            visible = uiState.selectedOption != null,
                            enter = slideInVertically { it } + fadeIn(),
                            exit = slideOutVertically { it } + fadeOut()
                        ) {
                            Button(
                                onClick = onNext,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = "Следующее слово →",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

// ── Sub-components ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardTopBar(
    mode: AppMode,
    score: Int,
    total: Int,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = mode.displayName,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
        },
        actions = {
            if (total > 0) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = "✓ $score / $total",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
private fun QuestionCard(uiState: CardUiState) {
    ElevatedCard(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Mode instruction
            Text(
                text = when (uiState.currentMode) {
                    AppMode.SR_TO_RU    -> "Переведи на русский"
                    AppMode.RU_TO_SR    -> "Переведи на сербский"
                    AppMode.SING_TO_PLUR -> "Множественное число от:"
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.8.sp
            )

            Spacer(Modifier.height(20.dp))

            // Gender badge — only for nouns
            val gender = genderStyle(uiState.genderTag)
            if (gender != null) {
                Surface(
                    shape = CircleShape,
                    color = gender.container.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = gender.label,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = gender.color,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // Main prompt word
            Text(
                text = uiState.prompt,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = if (uiState.prompt.length > 20) 26.sp else 36.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(16.dp))

            // Category pill
            if (uiState.category.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = uiState.category,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // Result feedback
            AnimatedVisibility(
                visible = uiState.isCorrect != null,
                enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Spacer(Modifier.height(20.dp))
                val correct = uiState.isCorrect == true
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (correct) CorrectGreen.copy(0.15f) else IncorrectRed.copy(0.12f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (correct) "✓ Правильно!" else "✗ Неверно",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (correct) CorrectGreen else IncorrectRed,
                            fontWeight = FontWeight.Bold
                        )
                        if (!correct) {
                            Text(
                                text = "Ответ: ${uiState.correctAnswer}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CorrectGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerGrid(
    options: List<String>,
    correctAnswer: String,
    selectedOption: String?,
    onSelect: (String) -> Unit
) {
    val answered = selectedOption != null

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        options.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { option ->
                    AnswerButton(
                        text = option,
                        correctAnswer = correctAnswer,
                        selectedOption = selectedOption,
                        answered = answered,
                        onClick = { if (!answered) onSelect(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Pad last row if it has only 1 item
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun AnswerButton(
    text: String,
    correctAnswer: String,
    selectedOption: String?,
    answered: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCorrect = text.trim().equals(correctAnswer.trim(), ignoreCase = true)
    val isSelected = text == selectedOption

    val containerColor by animateColorAsState(
        targetValue = when {
            !answered             -> MaterialTheme.colorScheme.surfaceVariant
            isCorrect             -> CorrectGreen
            isSelected && !isCorrect -> IncorrectRed
            else                  -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "btn_container_$text"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !answered             -> MaterialTheme.colorScheme.onSurfaceVariant
            isCorrect             -> Color.White
            isSelected && !isCorrect -> Color.White
            else                  -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        },
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "btn_content_$text"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btn_scale_$text"
    )

    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .height(64.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = if (!answered) 3.dp else 1.dp
        ),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        enabled = !answered || isCorrect || isSelected   // keep visual state after answer
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "⚠️",
                    fontSize = 40.sp
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                FilledTonalButton(onClick = onRetry) {
                    Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Попробовать снова")
                }
            }
        }
    }
}
