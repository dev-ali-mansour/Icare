package eg.edu.cu.csds.icare.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.feature.auth.screen.profile.ProfileScreen
import eg.edu.cu.csds.icare.feature.auth.screen.recovery.PasswordRecoveryScreen
import eg.edu.cu.csds.icare.feature.auth.screen.signin.SignInScreen
import eg.edu.cu.csds.icare.core.ui.navigation.Route

fun NavGraphBuilder.authenticationRoute(
    onRecoveryClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onSignInClicked: () -> Unit,
    onSignInSuccess: () -> Unit,
    onRecoveryCompleted: () -> Unit,
) {
    composable<Route.SignIn> {
        SignInScreen(
            onRecoveryClicked = { onRecoveryClicked() },
            onLoginSuccess = { onSignInSuccess() },
            onCreateAnAccountClicked = { onCreateAccountClicked() },
        )
    }

    composable<Route.PasswordRecovery> {
        PasswordRecoveryScreen(
            onSignInClicked = { onSignInClicked() },
            onRecoveryCompleted = { onRecoveryCompleted() },
        )
    }

    composable<Route.Profile> {
        ProfileScreen()
    }
}
