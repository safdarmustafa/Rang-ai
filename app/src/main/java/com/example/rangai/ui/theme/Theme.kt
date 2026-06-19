package com.example.rangai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RangAIColorScheme = darkColorScheme(
    primary = MaroonPrimary,
    onPrimary = TextPrimary,
    primaryContainer = BurgundySecondary,
    onPrimaryContainer = TextPrimary,
    secondary = BurgundySecondary,
    onSecondary = TextPrimary,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = WarmRedAccent,
    onTertiary = TextPrimary,
    background = DarkBackgroundStart,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder,
    outlineVariant = Color(0xFF3D2A32),
    error = ErrorRed,
    onError = TextPrimary
)

@Composable
fun RangAITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RangAIColorScheme,
        typography = Typography,
        shapes = RangAIShapes,
        content = content
    )
}
