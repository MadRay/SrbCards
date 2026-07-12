package com.srbcards.domain

/**
 * The three flashcard game modes available in SrbCards.
 *
 * @property displayName  Short label used in the top app bar and mode cards.
 * @property description  Russian-language subtitle shown on the main screen.
 * @property icon         Material icon name (resolved in the UI layer).
 */
enum class AppMode(
    val displayName: String,
    val description: String,
    val emoji: String
) {
    SR_TO_RU(
        displayName = "SR → RU",
        description = "Переведи слово на русский",
        emoji = "🇷🇸→🇷🇺"
    ),
    RU_TO_SR(
        displayName = "RU → SR",
        description = "Переведи слово на сербский",
        emoji = "🇷🇺→🇷🇸"
    ),
    SING_TO_PLUR(
        displayName = "Ед. → Мн.",
        description = "Образуй множественное число",
        emoji = "1→∞"
    )
}
