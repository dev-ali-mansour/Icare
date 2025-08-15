package eg.edu.cu.csds.icare.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.settings.screen.about.AboutScreen
import eg.edu.cu.csds.icare.settings.screen.settings.SettingsScreen

fun NavGraphBuilder.settingsRoute(
    navigateToScreen: (Route) -> Unit,
    onNavigationIconClicked: () -> Unit,
) {
    composable<Route.Settings> {
        SettingsScreen {
            navigateToScreen(it)
        }
    }
    composable<Route.About> {
        AboutScreen {
            onNavigationIconClicked()
        }
    }
}
