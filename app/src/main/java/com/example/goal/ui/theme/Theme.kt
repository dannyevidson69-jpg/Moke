package com.example.goal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.Typography

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
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GoalDarkColorScheme,
        typography = Typography,
        content = content
    )
}
