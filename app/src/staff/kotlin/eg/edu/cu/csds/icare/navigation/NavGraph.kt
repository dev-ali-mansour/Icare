package eg.edu.cu.csds.icare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import eg.edu.cu.csds.icare.core.ui.navigation.Navigator
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.admin.navigation.adminEntryBuilder
import eg.edu.cu.csds.icare.feature.admin.screen.center.SelectedCenterViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.SelectedClinicViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.SelectedClinicianViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.SelectedPharmacistViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.SelectedPharmacyViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.staff.SelectedStaffViewModel
import eg.edu.cu.csds.icare.feature.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.feature.auth.navigation.authenticationEntryBuilder
import eg.edu.cu.csds.icare.feature.consultation.navigation.consultationsEntryBuilder
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedPatientViewModel
import eg.edu.cu.csds.icare.feature.home.navigation.homeEntryBuilder
import eg.edu.cu.csds.icare.feature.notification.navigation.notificationsEntryBuilder
import eg.edu.cu.csds.icare.feature.onboarding.navigation.onBoardingEntryBuilder
import eg.edu.cu.csds.icare.feature.settings.navigation.settingsEntryBuilder
import eg.edu.cu.csds.icare.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val selectedClinicViewModel: SelectedClinicViewModel = koinViewModel()
    val selectedDoctorViewModel: SelectedDoctorViewModel = koinViewModel()
    val selectedClinicianViewModel: SelectedClinicianViewModel = koinViewModel()
    val selectedPharmacyViewModel: SelectedPharmacyViewModel = koinViewModel()
    val selectedPharmacistViewModel: SelectedPharmacistViewModel = koinViewModel()
    val selectedCenterViewModel: SelectedCenterViewModel = koinViewModel()
    val selectedStaffViewModel: SelectedStaffViewModel = koinViewModel()
    val selectedAppointmentViewModel: SelectedAppointmentViewModel = koinViewModel()
    val selectedConsultationViewModel: SelectedConsultationViewModel = koinViewModel()
    val selectedPatientViewModel: SelectedPatientViewModel = koinViewModel()

    NavDisplay(
        modifier = modifier,
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryProvider =
            entryProvider {
                entry<Route.Splash> {
                    SplashScreen()
                }
                onBoardingEntryBuilder {
                    navigator.navigate(key = Route.SignIn, inclusive = true)
                }
                authenticationEntryBuilder(
                    onRecoveryClicked = { navigator.navigate(Route.PasswordRecovery) },
                    onCreateAccountClicked = { navigator.navigate(Route.SignUp) },
                    onSignInClicked = {
                        navigator.navigate(key = Route.SignIn, inclusive = true)
                    },
                    onSignInSuccess = {
                        navigator.navigate(key = Route.Home, inclusive = true)
                    },
                    onRecoveryCompleted = {
                        navigator.navigate(key = Route.SignIn, inclusive = true)
                    },
                    onSignOut = {
                        navigator.navigate(key = Route.SignIn, inclusive = true)
                    },
                )
                homeEntryBuilder(
                    selectedDoctorViewModel = selectedDoctorViewModel,
                    selectedAppointmentViewModel = selectedAppointmentViewModel,
                    navigateToRoute = { navigator.navigate(it) },
                )

                notificationsEntryBuilder()

                adminEntryBuilder(
                    selectedClinicViewModel = selectedClinicViewModel,
                    selectedDoctorViewModel = selectedDoctorViewModel,
                    selectedClinicianViewModel = selectedClinicianViewModel,
                    selectedPharmacyViewModel = selectedPharmacyViewModel,
                    selectedPharmacistViewModel = selectedPharmacistViewModel,
                    selectedCenterViewModel = selectedCenterViewModel,
                    selectedStaffViewModel = selectedStaffViewModel,
                    onNavigationIconClicked = { navigator.goBack() },
                    navigateToRoute = { navigator.navigate(it) },
                )

                settingsEntryBuilder(
                    onNavigationIconClicked = { navigator.goBack() },
                    navigateToRoute = { navigator.navigate(it) },
                )

                consultationsEntryBuilder(
                    selectedAppointmentViewModel = selectedAppointmentViewModel,
                    selectedConsultationViewModel = selectedConsultationViewModel,
                    selectedPatientViewModel = selectedPatientViewModel,
                    onNavigationIconClicked = { navigator.goBack() },
                    navigateToRoute = { navigator.navigate(it) },
                )
            },
    )
}
