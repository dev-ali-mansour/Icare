package eg.edu.cu.csds.icare.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// / --- Light Theme Colors ---
val Teal = Color(color = 0xFF00687A)
val TealDark = Color(color = 0xFF004E5A)
val TealLight = Color(color = 0xFFB2EBF2)

val AccentYellow = Color(color = 0xFFFFC107)
val AccentYellowContainer = Color(color = 0xFFFFE082)

val LightBackground = Color(color = 0xFFF8F9FA)
val LightSurface = Color(color = 0xFFFFFFFF)

// --- Dark Theme Colors (New) ---
val DarkBackground = Color.DarkGray
val DarkSurface = Color(color = 0xFF333333) // Card background
val DarkPrimary = Color(color = 0xFF000000) // Top Bar & Bottom Nav are Black in dark mode
val DarkOnPrimary = Color(color = 0xFFFFFFFF)

// In Dark mode, the table header is black, and sub-text is Yellow
val TableHeaderDark = Color(color = 0xFF000000)
val TableTextYellow = Color(color = 0xFFFFC107)
val TableRowAlternatingDark = Color(color = 0xFF424242) // Slightly lighter than background

// Standard
val ErrorRed = Color(color = 0xFFAA0202)
val Blue200 = Color(color = 0xFF30AEC5)
val Blue500 = Color(color = 0xFF056B8D)
val Blue700 = Color(color = 0xFF082E5C)
val Orange200 = Color(color = 0xFFF27024)
val Yellow300 = Color(color = 0XFFFFD200)
val Yellow500 = Color(color = 0xFFF9B40E)
val Yellow700 = Color(color = 0xFFE3880C)
val EmeraldGreen = Color(color = 0xFF028A02)
val MediumGray = Color(color = 0xFF9C9C9C)
val LightGreen = Color(color = 0xFFE2EBCD)
val DeepTeal = Color(color = 0xFF3BDDDF)
val BurntOrange = Color(color = 0xFFEE6F5C)
val SkyAccent = Color(color = 0xFF3EBCDF)
val trustBlue = Color(color = 0xFF6488D9)

val titleColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray

val descriptionColor: Color
    @Composable
    get() =
        if (isSystemInDarkTheme()) {
            Color.LightGray.copy(alpha = 0.5f)
        } else {
            Color.DarkGray.copy(alpha = 0.5f)
        }

val activeIndicatorColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Blue700 else Blue500

val inactiveIndicatorColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

val TabRowContainerColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Gray else Blue200.copy(alpha = 0.1f)

val buttonBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Blue700 else Blue500

val contentColor: Color
    @Composable
    get() = Color.DarkGray

val tintColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.LightGray else Color.Black

val textColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val dropDownTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Blue200

val cardBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else SkyAccent.copy(alpha = 0.7f)
