package com.aneury1.biblia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Color(0xFF285C50), onPrimary = Color.White,
    primaryContainer = Color(0xFFD8EAE2), onPrimaryContainer = Color(0xFF123A30),
    secondary = Color(0xFF78633C), secondaryContainer = Color(0xFFF1E3C7),
    background = Color(0xFFFAF8F3), surface = Color(0xFFFAF8F3),
    surfaceVariant = Color(0xFFECEEE7), onSurface = Color(0xFF202923),
    onSurfaceVariant = Color(0xFF526159), outline = Color(0xFF758178)
)
private val Dark = darkColorScheme(
    primary = Color(0xFFA6D1BF), onPrimary = Color(0xFF10392D),
    primaryContainer = Color(0xFF284E40), onPrimaryContainer = Color(0xFFD8EAE2),
    secondary = Color(0xFFDEC799), secondaryContainer = Color(0xFF57492E),
    background = Color(0xFF141C18), surface = Color(0xFF141C18),
    surfaceVariant = Color(0xFF2A352F), onSurface = Color(0xFFE0E8DF),
    onSurfaceVariant = Color(0xFFBCCABD)
)
@Composable
fun BibliaTheme(darkTheme: Boolean = isSystemInDarkTheme(), dynamicColor: Boolean = false,
                content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, typography = Typography, content = content)
}
