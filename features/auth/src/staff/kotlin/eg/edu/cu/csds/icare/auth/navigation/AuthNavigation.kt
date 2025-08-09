package eg.edu.cu.csds.icare.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileScreen
import eg.edu.cu.csds.icare.auth.screen.recovery.PasswordRecoveryScreen
import eg.edu.cu.csds.icare.auth.screen.signin.SignInScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

fun NavGraphBuilder.authenticationRoute(
    onRecoveryClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onSignInClicked: () -> Unit,
    onSignInSuccess: () -> Unit,
    onRecoveryCompleted: () -> Unit,
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
            onSignInClicked = { onSignInClicked() },
            onRecoveryCompleted = { onRecoveryCompleted() },
        )
    }

    composable<Screen.Profile> {
        ProfileScreen()
    }
}
