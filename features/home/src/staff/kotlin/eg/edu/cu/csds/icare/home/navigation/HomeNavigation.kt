package eg.edu.cu.csds.icare.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.common.Role
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.home.screen.home.HomeScreen

fun NavGraphBuilder.homeRoute(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    clinicViewModel: ClinicViewModel,
    consultationViewModel: ConsultationViewModel,
    appointmentViewModel: AppointmentViewModel,
    navigateToScreen: (Route) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Route.Home> {
        HomeScreen(
            mainViewModel = mainViewModel,
            homeViewModel = homeViewModel,
            clinicViewModel = clinicViewModel,
            appointmentViewModel = appointmentViewModel,
            loadContentData = { user ->
                when (user.roleId) {
                    Role.AdminRole.code -> appointmentViewModel.getAdminStatistics()
                    Role.DoctorRole.code -> clinicViewModel.getDoctorSchedule(user.userId)
                    Role.ClinicStaffRole.code -> appointmentViewModel.getAppointments()
                    Role.PharmacistRole.code -> {}
                    Role.CenterStaffRole.code -> {}
                }
            },
            navigateToScreen = { navigateToScreen(it) },
            onPriceCardClicked = {
                // Todo Use SelectedDoctorViewModel to update doctor price
//                clinicViewModel.listClinics()
//                clinicViewModel.selectCurrentDoctor(it)
                navigateToScreen(Route.UpdateDoctor)
            },
            onAppointmentClick = {
                consultationViewModel.appointmentState.value = it
//                pharmacyViewModel.listPharmacies()
//                centerViewModel.listCenters()
                navigateToScreen(Route.NewConsultation)
            },
            onSeeAllClick = {
            },
            onSectionsAdminClicked = { navigateToScreen(Route.Admin) },
            onConfirm = {
                appointmentViewModel.selectedAppointmentState.value =
                    it.copy(statusId = AppointmentStatus.ConfirmedStatus.code)
                appointmentViewModel.updateAppointment()
                appointmentViewModel.getAppointments()
            },
            onError = { onError(it) },
        )
    }
}
