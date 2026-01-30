package eg.edu.cu.csds.icare.feature.notification.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.notification.screen.NotificationsScreen

fun EntryProviderScope<NavKey>.notificationsEntryBuilder() {
    entry<Route.Notifications> {
        NotificationsScreen()
    }
}
