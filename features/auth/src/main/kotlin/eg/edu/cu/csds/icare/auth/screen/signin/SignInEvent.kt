package eg.edu.cu.csds.icare.auth.screen.signin

sealed interface SignInEvent {
    data class UpdateGoogleSignInToken(
        val token: String,
    ) : SignInEvent

    data class UpdateEmail(
        val email: String,
    ) : SignInEvent

    data class UpdatePassword(
        val password: String,
    ) : SignInEvent

    object TogglePasswordVisibility : SignInEvent

    object SignInWithGoogle : SignInEvent

    object SubmitSignIn : SignInEvent

    object NavigateToPasswordRecoveryScreen : SignInEvent

    object NavigateToSignUpScreen : SignInEvent

    object ConsumeEffect : SignInEvent
}
