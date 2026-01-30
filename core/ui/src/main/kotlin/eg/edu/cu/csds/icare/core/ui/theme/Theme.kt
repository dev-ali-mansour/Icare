package eg.edu.cu.csds.icare.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme =
    lightColorScheme(
        primary = Teal,
        onPrimary = White,
        primaryContainer = TealLight,
        onPrimaryContainer = TealDark,
        secondary = AccentYellow,
        onSecondary = Black,
        secondaryContainer = AccentYellowContainer,
        onSecondaryContainer = Black,
        background = LightBackground,
        onBackground = Black,
        surface = LightSurface,
        onSurface = Black,
        error = ErrorRed,
    )
private val DarkColorScheme =
    darkColorScheme(
        primary = DarkPrimary,
        onPrimary = DarkOnPrimary,
        secondary = AccentYellow,
        onSecondary = Black,
        background = DarkBackground,
        surface = DarkSurface,
        onSurface = White,
        tertiaryContainer = TableHeaderDark,
        onTertiaryContainer = TableTextYellow,
        surfaceVariant = TableRowAlternatingDark,
        error = ErrorRed,
    )

@Composable
fun IcareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> {
                DarkColorScheme
            }

            else -> {
                LightColorScheme
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
