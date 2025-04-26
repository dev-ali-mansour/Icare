package eg.edu.cu.csds.icare.appointment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.appointment.screen.MyAppointmentsScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.appointmentsRoute(
    appointmentsViewModel: AppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.MyAppointments> {
        MyAppointmentsScreen(
            appointmentViewModel = appointmentsViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onReschedule = {},
            onCancel = {},
            onSuccess = {},
            onError = { onError(it) },
        )
    }

    composable<Screen.Doctors> {
    }
}
