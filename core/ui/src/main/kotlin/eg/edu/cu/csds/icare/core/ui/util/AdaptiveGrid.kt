package eg.edu.cu.csds.icare.core.ui.util

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@VisibleForTesting
internal const val BP_PHONE_MAX = 600f

@VisibleForTesting
internal const val BP_DESKTOP_MIN = 1200f

@VisibleForTesting
internal const val COL_PHONE_PORTRAIT = 1

@VisibleForTesting
internal const val COL_PHONE_LANDSCAPE_TABLET_PORTRAIT = 2

@VisibleForTesting
internal const val COL_TABLET_LANDSCAPE_DESKTOP = 4

@Composable
fun calculateGridColumns(): Int {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val widthDp =
        with(density) {
            windowInfo.containerSize.width
                .toDp()
                .value
        }
    val heightDp =
        with(density) {
            windowInfo.containerSize.height
                .toDp()
                .value
        }
    val smallestScreenWidthDp = configuration.smallestScreenWidthDp
    val isLandscape = widthDp > heightDp
    val isPhone = smallestScreenWidthDp < BP_PHONE_MAX
    val isDesktop = smallestScreenWidthDp >= BP_DESKTOP_MIN
    return when {
        isPhone -> if (isLandscape) COL_PHONE_LANDSCAPE_TABLET_PORTRAIT else COL_PHONE_PORTRAIT
        isDesktop -> COL_TABLET_LANDSCAPE_DESKTOP
        else -> if (isLandscape) COL_TABLET_LANDSCAPE_DESKTOP else COL_PHONE_LANDSCAPE_TABLET_PORTRAIT
    }
}
