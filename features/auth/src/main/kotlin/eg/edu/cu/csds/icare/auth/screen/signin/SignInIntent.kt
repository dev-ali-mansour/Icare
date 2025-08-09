package eg.edu.cu.csds.icare.auth.screen.signin

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
}
