package eg.edu.cu.csds.icare.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileScreen
import eg.edu.cu.csds.icare.auth.screen.recovery.PasswordRecoveryScreen
import eg.edu.cu.csds.icare.auth.screen.signin.SignInScreen
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

@Suppress("UnusedParameter")
fun NavGraphBuilder.authenticationRoute(
    firebaseAuth: FirebaseAuth,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    onRecoveryClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onSignInClicked: () -> Unit,
    onSignInSuccess: () -> Unit,
    onRecoveryCompleted: () -> Unit,
    onSignUpCompleted: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
) {
    composable<Screen.SignIn> {
        SignInScreen(
            onRecoveryClicked = { onRecoveryClicked() },
            onLoginSuccess = { onSignInSuccess() },
            onCreateAnAccountClicked = { onCreateAccountClicked() },
        )
    }

    composable<Screen.PasswordRecovery> {
        PasswordRecoveryScreen(
            onLoginClicked = { onSignInClicked() },
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
}
