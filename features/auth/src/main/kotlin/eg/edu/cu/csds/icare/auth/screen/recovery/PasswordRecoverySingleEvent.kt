package eg.edu.cu.csds.icare.auth.screen.recovery

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PasswordRecoverySingleEvent {
    object RecoverySuccess : PasswordRecoverySingleEvent

    data class ShowInfo(
        val message: UiText,
    ) : PasswordRecoverySingleEvent

    data class ShowError(
        val message: UiText,
    ) : PasswordRecoverySingleEvent
}
