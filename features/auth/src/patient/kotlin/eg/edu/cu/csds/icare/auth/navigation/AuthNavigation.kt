package eg.edu.cu.csds.icare.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileScreen
import eg.edu.cu.csds.icare.auth.screen.recovery.PasswordRecoveryScreen
import eg.edu.cu.csds.icare.auth.screen.signin.SignInScreen
import eg.edu.cu.csds.icare.auth.screen.signup.SignUpScreen
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.authenticationRoute(
    firebaseAuth: FirebaseAuth,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onRecoveryClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRecoveryCompleted: () -> Unit,
    onRegisterCompleted: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.SignIn> {
        SignInScreen(
            onRecoveryClicked = { onRecoveryClicked() },
            onLoginSuccess = { onLoginSuccess() },
            onCreateAnAccountClicked = { onCreateAccountClicked() },
        )
    }

    composable<Screen.PasswordRecovery> {
        PasswordRecoveryScreen(
            onLoginClicked = { onLoginClicked() },
            onRecoveryCompleted = { onRecoveryCompleted() },
            onError = { onError(it) },
            authViewModel = authViewModel,
        )
    }

    composable<Screen.Profile> {
        ProfileScreen(
            mainViewModel = mainViewModel,
            authViewModel = authViewModel,
            onError = { onError(it) },
        )
    }

    composable<Screen.SignUp> {
        SignUpScreen(
            firebaseAuth = firebaseAuth,
            onLoginClicked = { onLoginClicked() },
            onRegisterCompleted = { onRegisterCompleted() },
            authViewModel = authViewModel,
        )
    }
}
