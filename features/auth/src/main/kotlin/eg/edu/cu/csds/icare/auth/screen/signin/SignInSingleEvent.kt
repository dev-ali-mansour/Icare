package eg.edu.cu.csds.icare.auth.screen.signin

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface SignInSingleEvent {
    object LoginSuccess : SignInSingleEvent

    data class ShowError(
        val message: UiText,
    ) : SignInSingleEvent
}
