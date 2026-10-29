package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GoalDarkColorScheme = darkColorScheme(
    primary = GoalPrimary,
    onPrimary = GoalOnPrimary,
    primaryContainer = GoalPrimaryVariant,
    onPrimaryContainer = Color.White,
    secondary = GoalSecondary,
    onSecondary = Color.White,
    tertiary = GoalTertiary,
    onTertiary = Color.Black,
    background = GoalBackground,
    onBackground = GoalOnBackground,
    surface = GoalSurface,
    onSurface = GoalOnSurface,
    surfaceVariant = GoalSurfaceVariant,
    onSurfaceVariant = GoalOnSurfaceVariant,
    outline = GoalOutline
)

@Composable
fun GoalTheme(
    darkTheme: Boolean = true, // GOAL is primarily a rich dark streaming experience
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = GoalDarkColorScheme,
        typography = Typography,
        content = content
    )
}
