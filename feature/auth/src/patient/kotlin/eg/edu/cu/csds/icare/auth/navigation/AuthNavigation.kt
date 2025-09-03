package eg.edu.cu.csds.icare.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.auth.screen.profile.ProfileScreen
import eg.edu.cu.csds.icare.auth.screen.recovery.PasswordRecoveryScreen
import eg.edu.cu.csds.icare.auth.screen.signin.SignInScreen
import eg.edu.cu.csds.icare.auth.screen.signup.SignUpScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Route

fun NavGraphBuilder.authenticationRoute(
    onRecoveryClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRecoveryCompleted: () -> Unit,
    onRegisterCompleted: () -> Unit,
) {
    composable<Route.SignIn> {
        SignInScreen(
            onRecoveryClicked = { onRecoveryClicked() },
            onLoginSuccess = { onLoginSuccess() },
            onCreateAnAccountClicked = { onCreateAccountClicked() },
        )
    }

    composable<Route.PasswordRecovery> {
        PasswordRecoveryScreen(
            onSignInClicked = { onLoginClicked() },
            onRecoveryCompleted = { onRecoveryCompleted() },
        )
    }

    composable<Route.Profile> {
        ProfileScreen()
    }

    composable<Route.SignUp> {
        SignUpScreen(
            navigateToScreen = { onLoginClicked() },
            onSignUpSuccess = { onRegisterCompleted() },
        )
    }
}
