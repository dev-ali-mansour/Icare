package eg.edu.cu.csds.icare.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.MainActivity
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.appointment.navigation.appointmentsRoute
import eg.edu.cu.csds.icare.appointment.screen.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.auth.navigation.authenticationRoute
import eg.edu.cu.csds.icare.feature.consultation.navigation.consultationsRoute
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedConsultationViewModel
import eg.edu.cu.csds.icare.feature.consultation.screen.SelectedPatientViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.activity
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.feature.home.navigation.homeRoute
import eg.edu.cu.csds.icare.notification.navigation.notificationsRoute
import eg.edu.cu.csds.icare.onboarding.navigation.onBoardingRoute
import eg.edu.cu.csds.icare.settings.navigation.settingsRoute
import eg.edu.cu.csds.icare.splash.SplashScreen
import org.koin.androidx.compose.koinViewModel
import kotlin.system.exitProcess

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val selectedDoctorViewModel: SelectedDoctorViewModel = koinViewModel()
    val selectedAppointmentViewModel: SelectedAppointmentViewModel = koinViewModel()
    val selectedConsultationViewModel: SelectedConsultationViewModel = koinViewModel()
    val selectedPatientViewModel: SelectedPatientViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val alertMessage = remember { mutableStateOf("") }
    val showAlert = remember { mutableStateOf(false) }
    val exitApp = remember { mutableStateOf(false) }

    if (showAlert.value) {
        DialogWithIcon(text = alertMessage.value) {
            showAlert.value = false
            if (exitApp.value) exitProcess(0)
        }
    }

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
            onLoginClicked = {
                navController.navigate(Route.SignIn) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
            onLoginSuccess = {
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
            onRegisterCompleted = {
                navController.navigate(Route.SignIn) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
        )

        homeRoute(
            selectedDoctorViewModel = selectedDoctorViewModel,
            navigateUp = { navController.navigateUpSafely() },
            navigateToRoute = { screen -> navController.navigate(screen) },
        )

        notificationsRoute()

        settingsRoute(
            navigateUp = { navController.navigateUpSafely() },
            navigateToRoute = { navController.navigate(it) },
        )

        appointmentsRoute(
            selectedDoctorViewModel = selectedDoctorViewModel,
            selectedAppointmentViewModel = selectedAppointmentViewModel,
            onNavigationIconClicked = {
                navController.navigateUpSafely()
            },
            navigateToRoute = { navController.navigate(it) },
        )

        consultationsRoute(
            selectedConsultationViewModel = selectedConsultationViewModel,
            selectedPatientViewModel = selectedPatientViewModel,
            navigateUp = { navController.navigateUpSafely() },
            navigateToRoute = { route -> navController.navigate(route) },
        )
    }
}

private fun NavHostController.navigateUpSafely() {
    if (previousBackStackEntry == null) {
        navigate(Route.Home)
    } else {
        navigateUp()
    }
}
