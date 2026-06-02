package com.example.medicalknowledge.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = MedicalGreen,
    onPrimary = Color.White,
    primaryContainer = MedicalGreenLight,
    onPrimaryContainer = MedicalGreenDark,
    secondary = MedicalBlue,
    onSecondary = Color.White,
    secondaryContainer = MedicalBlueLight,
    onSecondaryContainer = MedicalBlueDark,
    tertiary = MedicalPurple,
    onTertiary = Color.White,
    error = MedicalRed,
    onError = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = TextSecondary
)

private val DarkColorScheme = darkColorScheme(
    primary = MedicalGreenLight,
    onPrimary = MedicalGreenDark,
    primaryContainer = MedicalGreen,
    onPrimaryContainer = Color.White,
    secondary = MedicalBlueLight,
    onSecondary = MedicalBlueDark,
    secondaryContainer = MedicalBlue,
    onSecondaryContainer = Color.White,
    tertiary = MedicalPurple,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

@Composable
fun MedicalKnowledgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}