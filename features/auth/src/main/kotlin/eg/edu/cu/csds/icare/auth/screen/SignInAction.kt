package eg.edu.cu.csds.icare.auth.screen

import android.content.Intent

sealed interface SignInAction {
    data class UpdateGoogleSignInIntent(
        val intent: Intent?,
    ) : SignInAction

    data class UpdateEmail(
        val email: String,
    ) : SignInAction

    data class UpdatePassword(
        val password: String,
    ) : SignInAction

    object TogglePasswordVisibility : SignInAction

    object SignInWithGoogle : SignInAction

    object SubmitSignIn : SignInAction

    object ResetPassword : SignInAction

    object CreateAccount : SignInAction
}
