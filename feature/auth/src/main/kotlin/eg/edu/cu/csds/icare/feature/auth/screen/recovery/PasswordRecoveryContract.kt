package eg.edu.cu.csds.icare.feature.auth.screen.recovery

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class PasswordRecoveryState(
    val isLoading: Boolean = false,
    val email: String = "",
    val effect: PasswordRecoveryEffect? = null,
)

sealed interface PasswordRecoveryEffect {
    object RecoverySuccess : PasswordRecoveryEffect

    data class ShowInfo(
        val message: UiText,
    ) : PasswordRecoveryEffect

    data class ShowError(
        val message: UiText,
    ) : PasswordRecoveryEffect
}

sealed interface PasswordRecoveryIntent {
    data class UpdateEmail(
        val email: String,
    ) : PasswordRecoveryIntent

    object SubmitRecovery : PasswordRecoveryIntent

    object NavigateToSignInScreen : PasswordRecoveryIntent

    object ConsumeEffect : PasswordRecoveryIntent
}
