package eg.edu.cu.csds.icare.auth.screen.recovery

sealed interface PasswordRecoverySingleEvent {
    object RecoverySuccess : PasswordRecoverySingleEvent

    data class ShowError(
        val message: String,
    ) : PasswordRecoverySingleEvent
}
