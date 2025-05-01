package eg.edu.cu.csds.icare.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Blue10 = Color(color = 0xFFE8F5F7)
val Blue200 = Color(color = 0xFF30AEC5)
val Blue500 = Color(color = 0xFF056B8D)
val Blue700 = Color(color = 0xFF082E5C)
val Orange200 = Color(color = 0xFFF27024)
val Yellow300 = Color(color = 0XFFFFD200)
val Yellow500 = Color(color = 0xFFF9B40E)
val Yellow700 = Color(color = 0xFFE3880C)
val PaidColor = Color(color = 0xFF028A02)
val UnPaidColor = Color(color = 0xFFAA0202)
val MediumGray = Color(color = 0xFF9C9C9C)
val LightGreen = Color(color = 0xFFE2EBCD)
val AiryGreen = Color(color = 0xFFF1F8F5)
val DeepTeal = Color(color = 0xFF3BDDDF)
val BurntOrange = Color(color = 0xFFEE6F5C)
val SkyAccent = Color(color = 0xFF3EBCDF)
val mintAccent = Color(color = 0xFF82EEA6)
val trustBlue = Color(color = 0xFF6488D9)

val backgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.White

val titleColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
val selectedCategoryCardColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Blue200
val unselectedCategoryCardColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.LightGray else Color.White
val unSelectedTintColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

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

val contentBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Gray else Blue200.copy(alpha = 0.1f)
val itemBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Gray else Blue10

val categoryBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Gray else Color.White

val buttonBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Blue700 else Blue500

val contentColor: Color
    @Composable
    get() = Color.DarkGray

val statusColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f) else Color.DarkGray

val tintColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Blue200
val dialogTint: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Blue200
val barBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Blue500

val textColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val dropDownTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Blue200

val trailIconColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Blue500

val cardBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) AiryGreen else SkyAccent.copy(alpha = 0.7f)

val cardTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.Black else Color.White
