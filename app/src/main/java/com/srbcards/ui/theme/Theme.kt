package com.srbcards.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary              = SerbianRed,
    onPrimary            = Color.White,
    primaryContainer     = SerbianRedDark,
    onPrimaryContainer   = SerbianRedLight,
    secondary            = SerbianBlue,
    onSecondary          = Color.White,
    secondaryContainer   = SerbianBlueDark,
    onSecondaryContainer = SerbianBlueLight,
    tertiary             = Gold,
    onTertiary           = Color(0xFF1A1A00),
    tertiaryContainer    = GoldDark,
    onTertiaryContainer  = GoldLight,
    background           = DarkBackground,
    onBackground         = Color(0xFFF0EEFF),
    surface              = DarkSurface,
    onSurface            = Color(0xFFF0EEFF),
    surfaceVariant       = DarkSurfaceVariant,
    onSurfaceVariant     = Color(0xFFB0BEC5),
    outline              = Color(0xFF4A5568)
)

private val LightColorScheme = lightColorScheme(
    primary              = SerbianRed,
    onPrimary            = Color.White,
    primaryContainer     = SerbianRedContainer,
    onPrimaryContainer   = SerbianRedDark,
    secondary            = SerbianBlue,
    onSecondary          = Color.White,
    secondaryContainer   = SerbianBlueContainer,
    onSecondaryContainer = SerbianBlueDark,
    tertiary             = GoldDark,
    onTertiary           = Color.White,
    tertiaryContainer    = GoldLight,
    onTertiaryContainer  = GoldDark,
    background           = Color(0xFFF5F3FF),
    onBackground         = Color(0xFF1A1A2E),
    surface              = Color(0xFFFFFFFF),
    onSurface            = Color(0xFF1A1A2E),
    surfaceVariant       = Color(0xFFEEEBFF),
    onSurfaceVariant     = Color(0xFF49454F),
    outline              = Color(0xFFCAC4D0)
)

@Composable
fun SrbCardsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = SrbCardsTypography,
        content     = content
    )
}
