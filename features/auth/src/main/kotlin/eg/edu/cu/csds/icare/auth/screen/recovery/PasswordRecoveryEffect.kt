package eg.edu.cu.csds.icare.auth.screen.recovery

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PasswordRecoveryEffect {
    object RecoverySuccess : PasswordRecoveryEffect

    data class ShowInfo(
        val message: UiText,
    ) : PasswordRecoveryEffect

    data class ShowError(
        val message: UiText,
    ) : PasswordRecoveryEffect
}
