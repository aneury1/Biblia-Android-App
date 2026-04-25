package com.aneury1.biblia.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,

    secondary = Color.White,
    onSecondary = Color.Black,

    tertiary = Color.White,
    onTertiary = Color.Black,

    background = Color.Black,
    onBackground = Color.White,

    surface = Color.Black,
    onSurface = Color.White,

    surfaceVariant = Color.Black,
    onSurfaceVariant = Color.White,

    outline = Color.White,

    error = Color.White,
    onError = Color.Black
)
private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,

    secondary = Color.Black,
    onSecondary = Color.White,

    tertiary = Color.Black,
    onTertiary = Color.White,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,

    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,

    outline = Color.Black,

    error = Color.Black,
    onError = Color.White
)

@Composable
fun BibliaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // force monochrome
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}