package eg.edu.cu.csds.icare.auth.screen.recovery

sealed interface PasswordRecoveryIntent {
    data class UpdateEmail(
        val email: String,
    ) : PasswordRecoveryIntent

    object SubmitRecovery : PasswordRecoveryIntent

    object NavigateToSignInScreen : PasswordRecoveryIntent
}
