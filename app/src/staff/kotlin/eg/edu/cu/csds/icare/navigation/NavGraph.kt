package eg.edu.cu.csds.icare.navigation

import android.content.Context
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
import eg.edu.cu.csds.icare.SplashScreen
import eg.edu.cu.csds.icare.admin.navigation.adminRoute
import eg.edu.cu.csds.icare.admin.screen.center.CenterViewModel
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicViewModel
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyViewModel
import eg.edu.cu.csds.icare.auth.navigation.authenticationRoute
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.core.ui.util.getErrorMessage
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.home.navigation.homeRoute
import eg.edu.cu.csds.icare.onboarding.navigation.onBoardingRoute
import eg.edu.cu.csds.icare.settings.navigation.settingsRoute
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.system.exitProcess

@Suppress("UnusedParameter")
@Composable
fun SetupNavGraph(
    firebaseAuth: FirebaseAuth,
    mediaHelper: MediaHelper,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    showAppSettings: () -> Unit,
    context: Context = LocalContext.current,
) {
    val onBoardingRes by mainViewModel.onBoardingCompleted.collectAsStateWithLifecycle()
    val clinicViewModel: ClinicViewModel = koinViewModel()
    val pharmacyViewModel: PharmacyViewModel = koinViewModel()
    val centerViewModel: CenterViewModel = koinViewModel()
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
                        navController.navigate(Screen.Login) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    onError = { error ->
                        exitApp.value = true
                        handleError(
                            error,
                            exitApp,
                            context,
                            authViewModel,
                            navController,
                            alertMessage,
                            showAlert,
                        )
                    },
                )
            }

            onBoardingRoute(onCompleted = {
                navController.navigate(Screen.Login) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            })

            authenticationRoute(
                firebaseAuth = firebaseAuth,
                mainViewModel = mainViewModel,
                authViewModel = authViewModel,
                onRecoveryClicked = { navController.navigate(Screen.PasswordRecovery) },
                onCreateAccountClicked = { navController.navigate(Screen.Register) },
                onLoginClicked = {
                    navController.navigate(Screen.Login) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onRecoveryCompleted = {
                    navController.navigate(Screen.Login) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onRegisterCompleted = {
                    navController.navigate(Screen.Login) {
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
                        authViewModel,
                        navController,
                        alertMessage,
                        showAlert,
                    )
                },
            )

            homeRoute(
                firebaseAuth = firebaseAuth,
                mediaHelper = mediaHelper,
                mainViewModel = mainViewModel,
                homeViewModel = homeViewModel,
                navigateToScreen = { screen -> navController.navigate(screen) },
                onError = { error ->
                    exitApp.value = false
                    handleError(
                        error,
                        exitApp,
                        context,
                        authViewModel,
                        navController,
                        alertMessage,
                        showAlert,
                    )
                },
            )

            adminRoute(
                mainViewModel = mainViewModel,
                clinicViewModel = clinicViewModel,
                pharmacyViewModel = pharmacyViewModel,
                centerViewModel = centerViewModel,
                onNavigationIconClicked = {
                    navController.navigateUpSafely()
                },
                navigateToScreen = { screen -> navController.navigate(screen) },
                onError = { error ->
                    exitApp.value = false
                    handleError(
                        error,
                        exitApp,
                        context,
                        authViewModel,
                        navController,
                        alertMessage,
                        showAlert,
                    )
                },
            )

            settingsRoute(
                mainViewModel = mainViewModel,
                navigateToScreen = { navController.navigate(it) },
                onNavigationIconClicked = {
                    navController.navigateUpSafely()
                },
            )

            appointmentsRoute(
                appointmentsViewModel = appointmentsViewModel,
                onNavigationIconClicked = {
                    navController.navigateUpSafely()
                },
                onError = { error ->
                    exitApp.value = false
                    handleError(
                        error,
                        exitApp,
                        context,
                        authViewModel,
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
    authViewModel: AuthViewModel,
    navController: NavHostController,
    alertMessage: MutableState<String>,
    showAlert: MutableState<Boolean>,
) {
    when (error) {
        is UserNotAuthenticatedException -> {
            authViewModel.onLogOutClick()
            navController.navigate(Screen.Login) {
                popUpTo(navController.graph.id) { inclusive = true }
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
