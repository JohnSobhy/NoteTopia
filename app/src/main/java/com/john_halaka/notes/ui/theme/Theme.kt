package com.john_halaka.notes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = BabyBlue,
    onPrimary = Color.Black,
    background = LightGrayBackground,
    onBackground = Color.DarkGray,
    surface = LightBlue,
    onSurface = DarkGray,
    primaryContainer = Turquoise,
    onPrimaryContainer = Color.Black,
    secondary = DarkTurquoise,
    onSecondary = Color.Black
)
private val DarkColorPalette = darkColorScheme(
    primary = BabyBlue,
    onPrimary = Color.White,
    background = DarkGray,
    onBackground = LightGrayBackground,
    surface = LightBlue,
    onSurface = DarkGray,
    primaryContainer = Turquoise,
    onPrimaryContainer = Color.Black,
    secondary = DarkTurquoise,
    onSecondary = Color.Black
)

@Composable
fun NotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}