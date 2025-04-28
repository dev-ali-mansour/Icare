package eg.edu.cu.csds.icare.appointment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.appointment.screen.DoctorProfileScreen
import eg.edu.cu.csds.icare.appointment.screen.MyAppointmentsScreen
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.appointmentsRoute(
    mainViewModel: MainViewModel,
    clinicViewModel: ClinicViewModel,
    appointmentsViewModel: AppointmentViewModel,
    onNavigationIconClicked: () -> Unit,
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
            onSuccess = { onNavigationIconClicked() },
            onError = { onError(it) },
        )
    }

    composable<Screen.Doctors> {
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
                appointmentsViewModel.getPatientAppointments()
                onNavigationIconClicked()
            },
            onError = { onError(it) },
        )
    }
}
