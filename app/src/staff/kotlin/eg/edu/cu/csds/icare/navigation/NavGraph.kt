package eg.edu.cu.csds.icare.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.MainActivity
import eg.edu.cu.csds.icare.feature.admin.navigation.adminRoute
import eg.edu.cu.csds.icare.feature.admin.screen.center.SelectedCenterViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.SelectedClinicViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.SelectedClinicianViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.SelectedPharmacistViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.SelectedPharmacyViewModel
import eg.edu.cu.csds.icare.feature.admin.screen.staff.SelectedStaffViewModel
import eg.edu.cu.csds.icare.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.auth.navigation.authenticationRoute
import eg.edu.cu.csds.icare.consultation.navigation.consultationsRoute
import eg.edu.cu.csds.icare.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.consultation.screen.SelectedPatientViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.activity
import eg.edu.cu.csds.icare.home.navigation.homeRoute
import eg.edu.cu.csds.icare.notification.navigation.notificationsRoute
import eg.edu.cu.csds.icare.onboarding.navigation.onBoardingRoute
import eg.edu.cu.csds.icare.settings.navigation.settingsRoute
import eg.edu.cu.csds.icare.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    context: Context = LocalContext.current,
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

    NavHost(
        navController = navController,
        startDestination = Route.Splash,
    ) {
        composable<Route.Splash> {
            SplashScreen()
        }

        onBoardingRoute(onFinished = {
            navController.navigate(Route.SignIn) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        })

        authenticationRoute(
            onRecoveryClicked = { navController.navigate(Route.PasswordRecovery) },
            onCreateAccountClicked = { navController.navigate(Route.SignUp) },
            onSignInClicked = {
                navController.navigate(Route.SignIn) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
            onSignInSuccess = {
                val intent = Intent(context, MainActivity::class.java)
                context.activity.finish()
                context.startActivity(intent)
            },
            onRecoveryCompleted = {
                navController.navigate(Route.SignIn) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
        )

        homeRoute(
            selectedDoctorViewModel = selectedDoctorViewModel,
            selectedAppointmentViewModel = selectedAppointmentViewModel,
            navigateToRoute = { route -> navController.navigate(route) },
        )

        notificationsRoute()

        adminRoute(
            selectedClinicViewModel = selectedClinicViewModel,
            selectedDoctorViewModel = selectedDoctorViewModel,
            selectedClinicianViewModel = selectedClinicianViewModel,
            selectedPharmacyViewModel = selectedPharmacyViewModel,
            selectedPharmacistViewModel = selectedPharmacistViewModel,
            selectedCenterViewModel = selectedCenterViewModel,
            selectedStaffViewModel = selectedStaffViewModel,
            navigateUp = { navController.navigateUpSafely() },
            navigateToRoute = { route -> navController.navigate(route) },
        )

        settingsRoute(
            navigateUp = { navController.navigateUpSafely() },
            navigateToRoute = { route -> navController.navigate(route) },
        )

        consultationsRoute(
            selectedAppointmentViewModel = selectedAppointmentViewModel,
            selectedConsultationViewModel = selectedConsultationViewModel,
            selectedPatientViewModel = selectedPatientViewModel,
            navigateUp = { navController.navigateUpSafely() },
            navigateToRoute = { route -> navController.navigate(route) },
        )
    }
}

private fun NavHostController.navigateUpSafely() {
    previousBackStackEntry?.let {
        navigate(Route.Home)
    } ?: navigateUp()
}
