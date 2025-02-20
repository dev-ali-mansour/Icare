package eg.edu.cu.csds.icare.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.auth.screen.login.LoginScreen
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

@Suppress("UnusedParameter")
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
    composable<Screen.Login> {
        LoginScreen(
            onRecoveryClicked = { onRecoveryClicked() },
            onCreateAnAccountClicked = { onCreateAccountClicked() },
            onLoginSuccess = { onLoginSuccess() },
            authViewModel = authViewModel,
        )
    }
    composable<Screen.PasswordRecovery> {
    }
    composable<Screen.Register> {
    }

    composable<Screen.Profile> {
    }
}
