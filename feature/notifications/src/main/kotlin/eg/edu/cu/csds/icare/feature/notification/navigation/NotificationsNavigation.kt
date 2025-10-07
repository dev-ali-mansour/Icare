package eg.edu.cu.csds.icare.feature.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.notification.screen.NotificationsScreen

fun NavGraphBuilder.notificationsRoute() {
    composable<Route.Notifications> {
        NotificationsScreen()
    }
}
