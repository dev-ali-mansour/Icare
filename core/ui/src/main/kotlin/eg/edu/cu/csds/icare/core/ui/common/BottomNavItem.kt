package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

sealed class BottomNavItem(
    @param:DrawableRes var iconResId: Int,
    @param:StringRes val titleResId: Int,
    var screen: Screen,
) {
    data object Home : BottomNavItem(
        R.drawable.ic_home,
        R.string.nav_home,
        Screen.Home,
    )

    data object Notifications : BottomNavItem(
        R.drawable.ic_notifications,
        R.string.nav_notifications,
        Screen.Notifications,
    )

    data object Profile : BottomNavItem(
        R.drawable.ic_profile,
        R.string.nav_profile,
        Screen.Profile,
    )

    data object Settings : BottomNavItem(
        R.drawable.ic_nav_settings,
        R.string.nav_settings,
        Screen.Settings,
    )
}
