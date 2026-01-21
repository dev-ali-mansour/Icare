package eg.edu.cu.csds.icare.feature.auth.navigation

import androidx.navigation3.runtime.EntryProviderScope
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.auth.screen.profile.ProfileScreen
import eg.edu.cu.csds.icare.feature.auth.screen.recovery.PasswordRecoveryScreen
import eg.edu.cu.csds.icare.feature.auth.screen.signin.SignInScreen
import eg.edu.cu.csds.icare.feature.auth.screen.signup.SignUpScreen

fun EntryProviderScope<Any>.authenticationEntryBuilder(
    onRecoveryClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    onSignInClicked: () -> Unit,
    onSignInSuccess: () -> Unit,
    onRecoveryCompleted: () -> Unit,
    onRegisterCompleted: () -> Unit,
    onSignOut: () -> Unit,
) {
    entry<Route.SignIn> {
        SignInScreen(
            onRecoveryClicked = { onRecoveryClicked() },
            onLoginSuccess = { onSignInSuccess() },
            onCreateAnAccountClicked = { onCreateAccountClicked() },
        )
    }

    entry<Route.PasswordRecovery> {
        PasswordRecoveryScreen(
            onSignInClicked = { onSignInClicked() },
            onRecoveryCompleted = { onRecoveryCompleted() },
        )
    }

    entry<Route.Profile> {
        ProfileScreen {
            onSignOut()
        }
    }

    entry<Route.SignUp> {
        SignUpScreen(
            navigateToScreen = { onSignInClicked() },
            onSignUpSuccess = { onRegisterCompleted() },
        )
    }
}
