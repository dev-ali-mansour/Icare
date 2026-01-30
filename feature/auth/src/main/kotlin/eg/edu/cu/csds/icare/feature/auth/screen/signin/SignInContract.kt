package eg.edu.cu.csds.icare.feature.auth.screen.signin

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class SignInState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val googleSignInToken: String = "",
    val effect: SignInEffect? = null,
)

sealed interface SignInEffect {
    object SignInSuccess : SignInEffect

    data class ShowError(
        val message: UiText,
    ) : SignInEffect
}

sealed interface SignInIntent {
    data class UpdateGoogleSignInToken(
        val token: String,
    ) : SignInIntent

    data class UpdateEmail(
        val email: String,
    ) : SignInIntent

    data class UpdatePassword(
        val password: String,
    ) : SignInIntent

    object TogglePasswordVisibility : SignInIntent

    object SignInWithGoogle : SignInIntent

    object SubmitSignIn : SignInIntent

    object NavigateToPasswordRecoveryScreen : SignInIntent

    object NavigateToSignUpScreen : SignInIntent

    object ConsumeEffect : SignInIntent
}
