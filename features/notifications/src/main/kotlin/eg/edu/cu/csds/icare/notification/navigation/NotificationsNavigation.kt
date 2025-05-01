package eg.edu.cu.csds.icare.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.notification.screen.NotificationsScreen

fun NavGraphBuilder.notificationsRoute() {
    composable<Screen.Notifications> {
        NotificationsScreen()
    }
}
