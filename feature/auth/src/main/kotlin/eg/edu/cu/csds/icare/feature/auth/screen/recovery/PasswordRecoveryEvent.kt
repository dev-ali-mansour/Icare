package eg.edu.cu.csds.icare.feature.auth.screen.recovery

sealed interface PasswordRecoveryEvent {
    data class UpdateEmail(
        val email: String,
    ) : PasswordRecoveryEvent

    object SubmitRecovery : PasswordRecoveryEvent

    object NavigateToSignInScreen : PasswordRecoveryEvent

    object ConsumeEffect : PasswordRecoveryEvent
}
