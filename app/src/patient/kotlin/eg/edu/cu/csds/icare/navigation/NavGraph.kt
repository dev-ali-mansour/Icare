package eg.edu.cu.csds.icare.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.MainActivity
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.appointment.navigation.appointmentsRoute
import eg.edu.cu.csds.icare.auth.navigation.authenticationRoute
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileEffect
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileEvent
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.consultation.screen.navigation.consultationsRoute
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.activity
import eg.edu.cu.csds.icare.core.ui.util.getErrorMessage
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.home.navigation.homeRoute
import eg.edu.cu.csds.icare.notification.navigation.notificationsRoute
import eg.edu.cu.csds.icare.onboarding.navigation.onBoardingRoute
import eg.edu.cu.csds.icare.settings.navigation.settingsRoute
import eg.edu.cu.csds.icare.splash.SplashScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.system.exitProcess

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    selectedDoctorViewModel: SelectedDoctorViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel(),
    clinicViewModel: ClinicViewModel = koinViewModel(),
    appointmentViewModel: AppointmentViewModel = koinViewModel(),
    consultationViewModel: ConsultationViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel(),
    context: Context = LocalContext.current,
) {
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
            navigateToRoute = { screen -> navController.navigate(screen) },
            onNavigationIconClicked = {
                navController.navigateUpSafely()
            },
        )

        notificationsRoute()

        settingsRoute(
            navigateToScreen = { navController.navigate(it) },
            onNavigationIconClicked = {
                navController.navigateUpSafely()
            },
        )

        appointmentsRoute(
            selectedDoctorViewModel = selectedDoctorViewModel,
            mainViewModel = mainViewModel,
            clinicViewModel = clinicViewModel,
            appointmentsViewModel = appointmentViewModel,
            onNavigationIconClicked = {
                navController.navigateUpSafely()
            },
            navigateToScreen = { navController.navigate(it) },
            onError = { error ->
                exitApp.value = false
                handleError(
                    error,
                    exitApp,
                    context,
                    profileViewModel,
                    navController,
                    alertMessage,
                    showAlert,
                )
            },
        )

        consultationsRoute(
            consultationViewModel = consultationViewModel,
            onNavigationIconClicked = { navController.navigateUpSafely() },
            navigateToScreen = { navController.navigate(it) },
            onError = { error ->
                exitApp.value = false
                handleError(
                    error,
                    exitApp,
                    context,
                    profileViewModel,
                    navController,
                    alertMessage,
                    showAlert,
                )
            },
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

private suspend fun handleError(
    error: Throwable?,
    exitApp: MutableState<Boolean>,
    context: Context,
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    alertMessage: MutableState<String>,
    showAlert: MutableState<Boolean>,
) {
    when (error) {
        is UserNotAuthenticatedException -> {
            profileViewModel.processEvent(ProfileEvent.SignOut)
            profileViewModel.uiState.collect { uiState ->
                if (uiState.effect is ProfileEffect.SignOutSuccess) {
                    navController.navigate(Route.SignIn) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
        }

        else -> {
            alertMessage.value = context.getErrorMessage(error)
            showAlert.value = true
            delay(timeMillis = 5000)
            showAlert.value = false
            if (exitApp.value) exitProcess(0)
        }
    }
}
