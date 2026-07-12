package com.srbcards.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.srbcards.domain.AppMode
import com.srbcards.domain.CardUiState
import com.srbcards.ui.theme.*

// ── Colour helpers ────────────────────────────────────────────────────────────

private val modeColors = listOf(
    Triple(SerbianRed, SerbianRedDark, SerbianRedLight),
    Triple(SerbianBlue, SerbianBlueDark, SerbianBlueLight),
    Triple(GoldDark, Color(0xFF7A5800), GoldLight)
)

private val modeIcons: List<ImageVector> = listOf(
    Icons.Filled.Translate,
    Icons.Filled.School,
    Icons.Filled.Star
)

// ── Root composable ───────────────────────────────────────────────────────────

/**
 * Main dashboard: header, session score banner, mode selection cards,
 * and a scrollable category filter row.
 *
 * @param categories   List of category strings fetched from Room.
 * @param uiState      Current flashcard session state (for displaying score).
 * @param onModeSelected  Called when the user taps a mode card.
 * @param onCategorySelected Called when the user selects (or deselects) a category.
 */
@Composable
fun MainScreen(
    categories: List<String>,
    uiState: CardUiState,
    onModeSelected: (AppMode) -> Unit,
    onCategorySelected: (String?) -> Unit
) {
    val scrollState = rememberScrollState()
    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(top = 56.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ── App header ────────────────────────────────────────────────────
            AppHeader()

            // ── Session score banner (only when a session is in progress) ─────
            if (uiState.total > 0) {
                ScoreBanner(score = uiState.score, total = uiState.total, mode = uiState.currentMode)
            }

            // ── Mode selection ────────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SectionLabel(text = "Выберите режим")
                Spacer(Modifier.height(4.dp))
                AppMode.entries.forEachIndexed { index, mode ->
                    ModeCard(
                        mode = mode,
                        isActive = uiState.currentMode == mode && uiState.total > 0,
                        colors = modeColors[index],
                        icon = modeIcons[index],
                        onClick = { onModeSelected(mode) }
                    )
                    if (index < AppMode.entries.lastIndex) Spacer(Modifier.height(12.dp))
                }
            }

            // ── Category filter ───────────────────────────────────────────────
            if (categories.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        SectionLabel(text = "Фильтр по теме")
                    }
                    CategoryChipRow(
                        categories = categories,
                        selected = uiState.currentCategory,
                        onSelect = onCategorySelected
                    )
                }
            }

            // ── Footer ────────────────────────────────────────────────────────
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Сербский язык · A1 уровень · ${67} слов",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── Sub-components ────────────────────────────────────────────────────────────

@Composable
private fun AppHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Flag strip
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .height(6.dp)
                .width(80.dp)
        ) {
            Box(Modifier.weight(1f).fillMaxHeight().background(SerbianRed))
            Box(Modifier.weight(1f).fillMaxHeight().background(SerbianBlue))
            Box(Modifier.weight(1f).fillMaxHeight().background(Color.White.copy(alpha = 0.9f)))
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "SrbCards",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Учи сербский — без интернета",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ScoreBanner(score: Int, total: Int, mode: AppMode) {
    val pct = if (total > 0) score.toFloat() / total else 0f
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Текущая сессия",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "$score / $total правильно",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { pct },
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    strokeWidth = 4.dp
                )
                Text(
                    text = "${(pct * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun ModeCard(
    mode: AppMode,
    isActive: Boolean,
    colors: Triple<Color, Color, Color>,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val (primary, dark, light) = colors
    val borderColor by animateColorAsState(
        targetValue = if (isActive) primary else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "border"
    )

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isActive) 6.dp else 2.dp,
            pressedElevation = 1.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isActive) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon bubble
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(listOf(light.copy(alpha = 0.4f), primary.copy(alpha = 0.15f)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mode.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = mode.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Emoji badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = primary.copy(alpha = 0.12f)
            ) {
                Text(
                    text = mode.emoji,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = primary
                )
            }
        }
    }
}

@Composable
private fun CategoryChipRow(
    categories: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        // "All" chip
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelect(null) },
                label = { Text("Все темы") },
                leadingIcon = if (selected == null) {
                    { Icon(Icons.Outlined.Category, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }
        items(categories) { cat ->
            FilterChip(
                selected = selected == cat,
                onClick = { onSelect(if (selected == cat) null else cat) },
                label = { Text(cat) }
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.5.sp
    )
}
