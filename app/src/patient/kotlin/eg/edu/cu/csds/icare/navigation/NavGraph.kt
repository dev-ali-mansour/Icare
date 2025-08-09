package eg.edu.cu.csds.icare.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.MainActivity
import eg.edu.cu.csds.icare.SplashScreen
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.appointment.AppointmentViewModel
import eg.edu.cu.csds.icare.appointment.navigation.appointmentsRoute
import eg.edu.cu.csds.icare.auth.navigation.authenticationRoute
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileIntent
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileSingleEvent
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileViewModel
import eg.edu.cu.csds.icare.consultation.ConsultationViewModel
import eg.edu.cu.csds.icare.consultation.screen.navigation.consultationsRoute
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.core.ui.util.activity
import eg.edu.cu.csds.icare.core.ui.util.getErrorMessage
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.home.navigation.homeRoute
import eg.edu.cu.csds.icare.notification.navigation.notificationsRoute
import eg.edu.cu.csds.icare.onboarding.navigation.onBoardingRoute
import eg.edu.cu.csds.icare.settings.navigation.settingsRoute
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.system.exitProcess

@Composable
fun SetupNavGraph(
    firebaseAuth: FirebaseAuth,
    mediaHelper: MediaHelper,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    context: Context = LocalContext.current,
) {
    val onBoardingRes by mainViewModel.onBoardingCompleted.collectAsStateWithLifecycle()
    val appointmentViewModel: AppointmentViewModel = koinViewModel()
    val clinicViewModel: ClinicViewModel = koinViewModel()
    val pharmacyViewModel: PharmacyViewModel = koinViewModel()
    val centerViewModel: CenterViewModel = koinViewModel()
    val consultationViewModel: ConsultationViewModel = koinViewModel()
    val alertMessage = remember { mutableStateOf("") }
    val showAlert = remember { mutableStateOf(false) }
    val exitApp = remember { mutableStateOf(false) }

    if (showAlert.value) {
        DialogWithIcon(text = alertMessage.value) {
            showAlert.value = false
            if (exitApp.value) exitProcess(0)
        }
    }

    onBoardingRes.data?.let { onBoardingCompleted ->
        val startScreen: Screen =
            when {
                onBoardingCompleted -> Screen.Splash
                else -> Screen.OnBoarding
            }
        NavHost(
            navController = navController,
            startDestination = startScreen,
        ) {
            composable<Screen.Splash> {
                SplashScreen(
                    firebaseAuth = firebaseAuth,
                    mainViewModel = mainViewModel,
                    navigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    navigateToLogin = {
                        navController.navigate(Screen.SignIn) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    onError = { error ->
                        exitApp.value = true
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

            onBoardingRoute(onCompleted = {
                navController.navigate(Screen.SignIn) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            })

            authenticationRoute(
                onRecoveryClicked = { navController.navigate(Screen.PasswordRecovery) },
                onCreateAccountClicked = { navController.navigate(Screen.SignUp) },
                onLoginClicked = {
                    navController.navigate(Screen.SignIn) {
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
                    navController.navigate(Screen.SignIn) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onRegisterCompleted = {
                    navController.navigate(Screen.SignIn) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
            )

            homeRoute(
                firebaseAuth = firebaseAuth,
                mediaHelper = mediaHelper,
                mainViewModel = mainViewModel,
                homeViewModel = homeViewModel,
                appointmentViewModel = appointmentViewModel,
                clinicViewModel = clinicViewModel,
                pharmacyViewModel = pharmacyViewModel,
                centerViewModel = centerViewModel,
                navigateToScreen = { screen -> navController.navigate(screen) },
                onNavigationIconClicked = {
                    navController.navigateUpSafely()
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

            notificationsRoute()

            settingsRoute(
                navigateToScreen = { navController.navigate(it) },
                onNavigationIconClicked = {
                    navController.navigateUpSafely()
                },
            )

            appointmentsRoute(
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
}

private fun NavHostController.navigateUpSafely() {
    if (previousBackStackEntry == null) {
        navigate(Screen.Home)
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
            profileViewModel.processIntent(ProfileIntent.SignOut)
            profileViewModel.singleEvent.collect { event ->
                if (event is ProfileSingleEvent.SignOutSuccess) {
                    navController.navigate(Screen.SignIn) {
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
