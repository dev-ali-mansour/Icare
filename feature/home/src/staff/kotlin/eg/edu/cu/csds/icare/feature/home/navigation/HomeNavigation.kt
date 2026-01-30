package eg.edu.cu.csds.icare.feature.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.feature.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.feature.home.screen.home.HomeScreen

fun EntryProviderScope<NavKey>.homeEntryBuilder(
    selectedDoctorViewModel: SelectedDoctorViewModel,
    selectedAppointmentViewModel: SelectedAppointmentViewModel,
    navigateToRoute: (Route) -> Unit,
) {
    entry<Route.Home> {
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
