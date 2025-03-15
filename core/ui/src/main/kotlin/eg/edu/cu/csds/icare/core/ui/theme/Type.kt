package eg.edu.cu.csds.icare.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import eg.edu.cu.csds.icare.core.ui.R

// Set of Material typography styles to start with
val Typography =
    Typography(
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        /* Other default text styles to override
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
         */
    )

val helveticaFamily =
    FontFamily(
        Font(R.font.helvetica_neuelt_w20_45_light, FontWeight.Light),
        Font(R.font.helveticaneuelt_w20_55_roman, FontWeight.Normal),
        Font(R.font.helveticaneuelt_w20_75_bold, FontWeight.Bold),
    )

val kufamFamily =
    FontFamily(
        Font(R.font.kufam_regular, FontWeight.Normal),
        Font(R.font.kufam_bold, FontWeight.Bold),
    )
