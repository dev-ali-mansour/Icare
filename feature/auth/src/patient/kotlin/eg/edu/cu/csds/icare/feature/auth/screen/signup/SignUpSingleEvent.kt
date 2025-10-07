package eg.edu.cu.csds.icare.feature.auth.screen.signup

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface SignUpSingleEvent {
    object LoginSuccess : SignUpSingleEvent

    data class ShowInfo(
        val message: UiText,
    ) : SignUpSingleEvent

    data class ShowError(
        val message: UiText,
    ) : SignUpSingleEvent
}
