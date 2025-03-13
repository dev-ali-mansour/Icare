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
import eg.edu.cu.csds.icare.auth.navigation.authenticationRoute
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.domain.model.UserNotAuthenticatedException
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.util.activity
import eg.edu.cu.csds.icare.core.ui.util.getErrorMessage
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.onboarding.navigation.onBoardingRoute
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

@Suppress("UnusedParameter")
@Composable
fun SetupNavGraph(
    firebaseAuth: FirebaseAuth,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    showAppSettings: () -> Unit,
    context: Context = LocalContext.current,
) {
    val onBoardingRes by mainViewModel.onBoardingCompleted.collectAsStateWithLifecycle()
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
                    context.activity.finish()
                    context.startActivity(Intent(context, MainActivity::class.java))
                },
                onRecoveryCompleted = {
                    navController.navigate(Screen.Login) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onRegisterCompleted = {
                    navController.navigate(Screen.Home) {
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
        }
    }
}

@Suppress("UnusedPrivateMember")
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
            delay(timeMillis = 3000)
            showAlert.value = false
            if (exitApp.value) exitProcess(0)
        }
    }
}
