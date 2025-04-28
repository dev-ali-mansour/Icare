package eg.edu.cu.csds.icare.appointment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.appointment.screen.DoctorProfileScreen
import eg.edu.cu.csds.icare.appointment.screen.DoctorsListScreen
import eg.edu.cu.csds.icare.appointment.screen.MyAppointmentsScreen
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.appointmentsRoute(
    mainViewModel: MainViewModel,
    clinicViewModel: ClinicViewModel,
    appointmentsViewModel: AppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.MyAppointments> {
        MyAppointmentsScreen(
            appointmentViewModel = appointmentsViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onReschedule = {},
            onCancel = {
                appointmentsViewModel.selectedAppointment.value = it
                appointmentsViewModel.updateAppointment()
            },
            onError = { onError(it) },
        )
    }

    composable<Screen.DoctorList> {
        DoctorsListScreen(
            clinicViewModel = clinicViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onSearch = {
                if (clinicViewModel.searchQueryState.value.isEmpty()) {
                    clinicViewModel.listDoctors()
                } else {
                    clinicViewModel.searchDoctors()
                }
            },
            onClear = { clinicViewModel.listDoctors() },
            onDoctorClicked = {
                clinicViewModel.selectedDoctorState.value = it
                clinicViewModel.getDoctorSchedule(it.id)
                navigateToScreen(Screen.DoctorProfile)
            },
            onError = { onError(it) },
        )
    }

    composable<Screen.DoctorProfile> {
        DoctorProfileScreen(
            mainViewModel = mainViewModel,
            clinicViewModel = clinicViewModel,
            appointmentViewModel = appointmentsViewModel,
            onNavigationIconClicked = { onNavigationIconClicked() },
            onProceedButtonClicked = { doctorId, userId ->
                appointmentsViewModel.doctorIdState.value = doctorId
                appointmentsViewModel.patientIdState.value = userId
                appointmentsViewModel.bookAppointment()
            },
            onSuccess = {
                appointmentsViewModel.selectedSlotState.longValue = 0
                appointmentsViewModel.getPatientAppointments()
                onNavigationIconClicked()
            },
            onError = { onError(it) },
        )
    }
}
