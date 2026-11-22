package com.example.taskbudget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Green700,
    onPrimary = White,
    primaryContainer = Mint,
    onPrimaryContainer = Green900,
    secondary = Coral,
    onSecondary = White,
    tertiary = Amber,
    onTertiary = Charcoal,
    background = Cream,
    onBackground = Charcoal,
    surface = White,
    onSurface = Charcoal,
    surfaceVariant = Mist,
    onSurfaceVariant = Slate,
    outline = Slate,
    error = Coral,
    onError = White
)

private val DarkColors = darkColorScheme(
    primary = Green400,
    onPrimary = Charcoal,
    primaryContainer = Green900,
    onPrimaryContainer = Mint,
    secondary = Coral,
    onSecondary = White,
    tertiary = Amber,
    onTertiary = Charcoal,
    background = Charcoal,
    onBackground = White,
    surface = Color(0xFF2A2F2B),
    onSurface = White,
    surfaceVariant = Color(0xFF353A36),
    onSurfaceVariant = Color(0xFFC9CFC9),
    outline = Color(0xFF8A938C),
    error = Coral,
    onError = White
)

@Composable
fun TaskBudgetTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
