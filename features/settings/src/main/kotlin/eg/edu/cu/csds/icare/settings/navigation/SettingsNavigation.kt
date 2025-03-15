package eg.edu.cu.csds.icare.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.settings.screen.about.AboutScreen
import eg.edu.cu.csds.icare.settings.screen.settings.SettingsScreen

fun NavGraphBuilder.settingsRoute(
    navigateToAbout: () -> Unit,
    onNavigationIconClicked: () -> Unit,
) {
    composable<Screen.Settings> {
        SettingsScreen(navigateToAbout = {
            navigateToAbout()
        })
    }
    composable<Screen.About> {
        AboutScreen(
            onNavigationIconClicked = { onNavigationIconClicked() },
        )
    }
}
