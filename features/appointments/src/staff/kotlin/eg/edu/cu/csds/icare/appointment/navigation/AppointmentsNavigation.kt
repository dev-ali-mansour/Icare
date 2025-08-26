package eg.edu.cu.csds.icare.appointment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route

fun NavGraphBuilder.appointmentsRoute(
    appointmentViewModel: AppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Route.Appointments> {
    }
}
