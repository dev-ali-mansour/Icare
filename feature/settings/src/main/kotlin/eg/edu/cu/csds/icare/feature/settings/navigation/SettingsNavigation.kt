package eg.edu.cu.csds.icare.feature.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.settings.screen.about.AboutScreen
import eg.edu.cu.csds.icare.feature.settings.screen.settings.SettingsScreen

fun EntryProviderScope<Any>.settingsEntryBuilder(
    onNavigationIconClicked: () -> Unit,
    navigateToRoute: (Route) -> Unit,
) {
    entry<Route.Settings> {
        SettingsScreen {
            navigateToRoute(it)
        }
    }
    entry<Route.About> {
        AboutScreen {
            onNavigationIconClicked()
        }
    }
}
