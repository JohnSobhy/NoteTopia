package com.john_halaka.noteTopia.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = White,
    onPrimary = Black,
    background = White,
    //the color of sort radioButtons and text of editNote hints
    onBackground = DarkerGray,
    surface = White,
    primaryContainer = BrandGreen,
    onPrimaryContainer = Black,
    //the color of search box
    secondary = Gray,
    //the color of text inside search box
    onSecondary = DarkGray,
    onSecondaryContainer = Black,
    //the color of colorPicker border
    onSurface = Black,
    secondaryContainer = BrandGreen,
    //the color of topBar action items
    onSurfaceVariant = Black,
    onTertiary = DividerShadowLight


)
private val DarkColorScheme = darkColorScheme(
    primary = Black,
    onPrimary = White,
    background = Black,
    onBackground = White,
    surface = Black, // colors the top and bottom bars
    primaryContainer = BrandGreen,
    onPrimaryContainer = Black,
    //the color of search box
    secondary = Gray,
    onSecondary = DarkGray,
    onSecondaryContainer = Black,
    //the color of colorPicker border
    onSurface = White,
    secondaryContainer = BrandGreen,
    onSurfaceVariant = BarItemGray,
    onTertiary = DividerShadowDark
)

@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb() // change color status bar here
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}