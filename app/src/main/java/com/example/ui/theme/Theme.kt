package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MirathBurntOrange,
    onPrimary = Color.White,
    primaryContainer = MirathTerracottaDark,
    onPrimaryContainer = MirathCream,
    secondary = MirathGoldAccent,
    onSecondary = MirathDarkBrown,
    secondaryContainer = MirathDarkSurfaceVariant,
    onSecondaryContainer = MirathCream,
    tertiary = MirathSand,
    onTertiary = MirathDarkBrown,
    background = MirathDarkBackground,
    onBackground = MirathDarkOnSurface,
    surface = MirathDarkSurface,
    onSurface = MirathDarkOnSurface,
    surfaceVariant = MirathDarkSurfaceVariant,
    onSurfaceVariant = MirathDarkMuted,
    outline = MirathMutedBrown
)

private val LightColorScheme = lightColorScheme(
    primary = MirathTerracotta,
    onPrimary = Color.White,
    primaryContainer = MirathWarmSurface,
    onPrimaryContainer = MirathDarkBrown,
    secondary = MirathBurntOrange,
    onSecondary = Color.White,
    secondaryContainer = MirathCream,
    onSecondaryContainer = MirathDarkBrown,
    tertiary = MirathGoldAccent,
    onTertiary = MirathDarkBrown,
    background = MirathIvory,
    onBackground = MirathDarkBrown,
    surface = MirathCream,
    onSurface = MirathDarkBrown,
    surfaceVariant = MirathWarmSurface,
    onSurfaceVariant = MirathMutedBrown,
    outline = MirathSand
)

@Composable
fun MirathTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
