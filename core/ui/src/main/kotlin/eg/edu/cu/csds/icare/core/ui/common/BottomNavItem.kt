package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.navigation.Route

sealed class BottomNavItem(
    @param:DrawableRes var iconResId: Int,
    @param:StringRes val titleResId: Int,
    var route: Route,
) {
    data object Home : BottomNavItem(
        R.drawable.ic_home,
        R.string.nav_home,
        Route.Home,
    )

    data object Notifications : BottomNavItem(
        R.drawable.ic_notifications,
        R.string.nav_notifications,
        Route.Notifications,
    )

    data object Profile : BottomNavItem(
        R.drawable.ic_profile,
        R.string.nav_profile,
        Route.Profile,
    )

    data object Settings : BottomNavItem(
        R.drawable.ic_nav_settings,
        R.string.nav_settings,
        Route.Settings,
    )
}
