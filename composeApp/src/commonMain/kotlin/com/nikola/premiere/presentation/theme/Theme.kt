package com.nikola.premiere.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkPalette = darkColorScheme(
    primary = RacingRed,
    secondary = IcyBlue,
    background = PureBlack,
    surface = SurfaceGrey,
    surfaceVariant = Graphite,
    onPrimary = PureWhite,
    onBackground = SoftLinen,
    onSurface = SoftLinen,
    onSurfaceVariant = SoftLinen.copy(alpha = 0.7f)
)
private val LightPalette = lightColorScheme(
    primary = RacingRed,
    secondary = IcyBlue,
    background = SoftLinen,
    surface = PureWhite,
    surfaceVariant = PureWhite,
    onPrimary = PureWhite,
    onBackground = InkBlack,
    onSurface = InkBlack,
    onSurfaceVariant = InkBlack.copy(alpha = 0.7f)
)
@Composable
fun PremiereTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Odlučujemo koju paletu koristimo na osnovu parametra
    val colorScheme = if (darkTheme) DarkPalette else LightPalette

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}