package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

fun getDynamicColorScheme(themeName: String): ColorScheme {
    val (primary, primaryContainer, secondary) = when (themeName.uppercase()) {
        "RED" -> Triple(RedPrimary, RedContainer, RedLight)
        "GREEN" -> Triple(GreenPrimary, GreenContainer, GreenLight)
        "UNICORN" -> Triple(UnicornPrimary, UnicornContainer, UnicornLight)
        "GOLD" -> Triple(GoldPrimary, GoldContainer, GoldLight)
        "CYAN" -> Triple(CyanPrimary, CyanContainer, CyanLight)
        else -> Triple(TechBlueBright, TechBlue, TechCyan) // BLUE
    }

    return darkColorScheme(
        primary = primary,
        onPrimary = TextPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = TextPrimary,
        secondary = secondary,
        onSecondary = BgDeep,
        secondaryContainer = CardDark,
        onSecondaryContainer = TextBody,
        tertiary = AccentGreenBright,
        onTertiary = BgDeep,
        background = BgDeep,
        onBackground = TextPrimary,
        surface = SurfaceDark,
        onSurface = TextPrimary,
        surfaceVariant = CardDark,
        onSurfaceVariant = TextBody,
        outline = BorderDark,
        error = AccentRed,
        onError = TextPrimary
    )
}

@Composable
fun MyApplicationTheme(
    themeName: String = "BLUE",
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = getDynamicColorScheme(themeName),
        typography = Typography,
        content = content
    )
}
