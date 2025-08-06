package com.example.gymdex.ui.theme

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LightGrayButton,      // Buttons and primary elements
    secondary = DarkGrayBackground, // Background
    tertiary = WhiteAccent,         // Icons, accents
    background = DarkGrayBackground,// Main background
    surface = DarkGrayBackground,   // Surface (e.g., cards)
    onPrimary = BlackText,          // Text on buttons
    onSecondary = LightGrayText,    // Text on background
    onSurface = BlackText           // Text on surfaces
)

@Composable
fun GymDexTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme;

    val typography = Typography

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}