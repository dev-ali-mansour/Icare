package eg.edu.cu.csds.icare.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.common.Role
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.home.screen.HomeScreen

fun NavGraphBuilder.homeRoute(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    clinicViewModel: ClinicViewModel,
    pharmacyViewModel: PharmacyViewModel,
    centerViewModel: CenterViewModel,
    consultationViewModel: ConsultationViewModel,
    appointmentViewModel: AppointmentViewModel,
    navigateToScreen: (Screen) -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.Home> {
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
                clinicViewModel.listClinics()
                clinicViewModel.selectCurrentDoctor(it)
                navigateToScreen(Screen.EditDoctor)
            },
            onAppointmentClick = {
                consultationViewModel.appointmentState.value = it
                pharmacyViewModel.listPharmacies()
                centerViewModel.listCenters()
                navigateToScreen(Screen.NewConsultation)
            },
            onSeeAllClick = {
            },
            onSectionsAdminClicked = { navigateToScreen(Screen.Admin) },
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
