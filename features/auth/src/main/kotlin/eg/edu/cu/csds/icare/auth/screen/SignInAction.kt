package eg.edu.cu.csds.icare.auth.screen

sealed interface SignInAction {
    data class UpdateGoogleSignInToken(
        val token: String,
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

    object NavigateToPasswordRecoveryScreen : SignInAction

    object NavigateToSignUpScreen : SignInAction
}
