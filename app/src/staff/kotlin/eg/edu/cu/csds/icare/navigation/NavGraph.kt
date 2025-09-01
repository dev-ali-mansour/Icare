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
import eg.edu.cu.csds.icare.admin.navigation.adminRoute
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.center.SelectedCenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.SelectedClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.clinician.SelectedClinicianViewModel
import eg.edu.cu.csds.icare.admin.screen.doctor.SelectedDoctorViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacist.SelectedPharmacistViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.SelectedPharmacyViewModel
import eg.edu.cu.csds.icare.admin.screen.staff.SelectedStaffViewModel
import eg.edu.cu.csds.icare.appointment.SelectedAppointmentViewModel
import eg.edu.cu.csds.icare.auth.navigation.authenticationRoute
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileEffect
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileEvent
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.consultation.screen.navigation.consultationsRoute
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
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
    val pharmacyViewModel: PharmacyViewModel = koinViewModel()
    val centerViewModel: CenterViewModel = koinViewModel()
    val consultationViewModel: ConsultationViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
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
            navigateToRoute = { screen -> navController.navigate(screen) },
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
            onNavigationIconClicked = {
                navController.navigateUpSafely()
            },
            navigateToRoute = { screen ->
                navController.navigate(screen)
            },
        )

        settingsRoute(
            navigateToScreen = {
                navController.navigate(it)
            },
            onNavigationIconClicked = {
                navController.navigateUpSafely()
            },
        )

        consultationsRoute(
            selectedAppointmentViewModel = selectedAppointmentViewModel,
            pharmacyViewModel = pharmacyViewModel,
            centerViewModel = centerViewModel,
            consultationViewModel = consultationViewModel,
            onNavigationIconClicked = { navController.navigateUpSafely() },
            navigateToScreen = {
                navController.navigate(it) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
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
