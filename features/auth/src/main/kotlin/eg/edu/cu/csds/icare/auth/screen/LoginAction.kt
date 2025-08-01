package eg.edu.cu.csds.icare.auth.screen

import android.content.Intent

sealed interface LoginAction {
    data class UpdateGoogleLoginIntent(
        val intent: Intent?,
    ) : LoginAction

    data class UpdateEmail(
        val email: String,
    ) : LoginAction

    data class UpdatePassword(
        val password: String,
    ) : LoginAction

    object TogglePasswordVisibility : LoginAction

    object LoginWithGoogle : LoginAction

    object SubmitLogin : LoginAction

    object ResetPassword : LoginAction

    object CreateAccount : LoginAction
}
