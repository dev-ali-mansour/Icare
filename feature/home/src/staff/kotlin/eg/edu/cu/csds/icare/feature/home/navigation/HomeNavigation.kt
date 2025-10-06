package eg.edu.cu.csds.icare.feature.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.home.screen.home.HomeScreen

fun NavGraphBuilder.homeRoute(
    selectedDoctorViewModel: SelectedDoctorViewModel,
    selectedAppointmentViewModel: SelectedAppointmentViewModel,
    navigateToRoute: (Route) -> Unit,
) {
    composable<Route.Home> {
        LaunchedEffect(true) {
            selectedDoctorViewModel.onSelectDoctor(null)
            selectedAppointmentViewModel.onSelectAppointment(null)
        }
        HomeScreen(
            navigateToRoute = { navigateToRoute(it) },
            navigateToUpdateDoctorScreen = { doctor ->
                selectedDoctorViewModel.onSelectDoctor(doctor)
                navigateToRoute(Route.UpdateDoctor)
            },
            navigateToNewConsultation = {
                selectedAppointmentViewModel.onSelectAppointment(it)
                navigateToRoute(Route.NewConsultation)
            },
        )
    }
}
