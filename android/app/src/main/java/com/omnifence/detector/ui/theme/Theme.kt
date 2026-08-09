package com.omnifence.detector.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class StatusColors(
    val matched: Color,
    val attention: Color,
    val insufficient: Color,
    val error: Color,
)

val LocalStatusColors = staticCompositionLocalOf {
    StatusColors(Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified)
}

val statusColors: StatusColors
    @Composable
    @ReadOnlyComposable
    get() = LocalStatusColors.current

private val LightScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF245BDB),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE6FF),
    onPrimaryContainer = Color(0xFF001A43),
    secondary = Color(0xFF4E5969),
    background = Color(0xFFF8F9FC),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF0F2F7),
    onSurface = Color(0xFF171B24),
    onSurfaceVariant = Color(0xFF5A6474),
    outline = Color(0xFF747E90),
)

private val DarkScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFB7C7FF),
    onPrimary = Color(0xFF002B74),
    primaryContainer = Color(0xFF1746A2),
    onPrimaryContainer = Color(0xFFDCE6FF),
    secondary = Color(0xFFC2CADB),
    background = Color(0xFF111318),
    surface = Color(0xFF1A1D24),
    surfaceVariant = Color(0xFF252A35),
    onSurface = Color(0xFFE3E8F2),
    onSurfaceVariant = Color(0xFFC1C8D7),
    outline = Color(0xFF8B94A5),
)

private val LightStatusColors = StatusColors(
    matched = Color(0xFF16794A),
    attention = Color(0xFFC62828),
    insufficient = Color(0xFF53677D),
    error = Color(0xFF9B251A),
)

private val DarkStatusColors = StatusColors(
    matched = Color(0xFF6DDD9A),
    attention = Color(0xFFFFB4AB),
    insufficient = Color(0xFFB8C9E0),
    error = Color(0xFFFFB4A9),
)

@Composable
fun OmniFenceTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    androidx.compose.runtime.CompositionLocalProvider(
        LocalStatusColors provides if (dark) DarkStatusColors else LightStatusColors,
    ) {
        MaterialTheme(
            colorScheme = if (dark) DarkScheme else LightScheme,
            content = content,
        )
    }
}
