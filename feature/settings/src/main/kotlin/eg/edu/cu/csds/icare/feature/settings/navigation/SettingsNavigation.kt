package eg.edu.cu.csds.icare.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.settings.screen.about.AboutScreen
import eg.edu.cu.csds.icare.feature.settings.screen.settings.SettingsScreen

fun NavGraphBuilder.settingsRoute(
    navigateUp: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    composable<Route.Settings> {
        SettingsScreen {
            navigateToRoute(it)
        }
    }
    composable<Route.About> {
        AboutScreen {
            navigateUp()
        }
    }
}
