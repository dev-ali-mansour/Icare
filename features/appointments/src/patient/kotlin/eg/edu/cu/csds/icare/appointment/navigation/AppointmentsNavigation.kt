package eg.edu.cu.csds.icare.appointment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.appointment.AppointmentsViewModel
import eg.edu.cu.csds.icare.appointment.screen.MyAppointmentsScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.appointmentsRoute(
    appointmentsViewModel: AppointmentsViewModel,
    onNavigationIconClicked: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.MyAppointments> {
        MyAppointmentsScreen(
            appointmentsViewModel = appointmentsViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onReschedule = {},
            onCancel = {},
            onSuccess = {},
            onError = { onError(it) },
        )
    }

    composable<Screen.BookAppointment> {
    }
}
