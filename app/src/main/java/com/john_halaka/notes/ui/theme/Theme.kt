package com.john_halaka.notes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = OptionPink,
    onPrimary = Color.Black,
    background = BabyPowder,
    onBackground = Color.DarkGray,
    surface = MainGreen,
    primaryContainer = MainGreen,
    onPrimaryContainer = Color.Black,
    secondary = OptionPink,
    onSecondary = Color.Black,

    onSecondaryContainer = Color.Black,
    onSurface = DarkGray,
    secondaryContainer = SecondaryGreen,
    onSurfaceVariant = DarkGray,
    onTertiary = Gray


)
private val DarkColorPalette = darkColorScheme(
    primary = OptionPink,
    onPrimary = Color.White,
    background = BackBlack,
    onBackground = LightGrayBackground,
    surface = MainGreen,
    primaryContainer = MainGreen,
    onPrimaryContainer = Color.Black,
    secondary = OptionPink,
    onSecondary = Color.Black,


    onSecondaryContainer = Color.Black,
    onSurface = DarkGray,
    secondaryContainer = SecondaryGreen,
    onSurfaceVariant = DarkGray,
    onTertiary = Gray
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