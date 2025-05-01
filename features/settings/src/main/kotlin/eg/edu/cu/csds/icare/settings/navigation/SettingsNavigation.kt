package eg.edu.cu.csds.icare.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.settings.screen.about.AboutScreen
import eg.edu.cu.csds.icare.settings.screen.settings.SettingsScreen

fun NavGraphBuilder.settingsRoute(
    navigateToScreen: (Screen) -> Unit,
    onNavigationIconClicked: () -> Unit,
) {
    composable<Screen.Settings> {
        SettingsScreen {
            navigateToScreen(it)
        }
    }
    composable<Screen.About> {
        AboutScreen {
            onNavigationIconClicked()
        }
    }
}
