package eg.edu.cu.csds.icare.auth.screen.signin

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface SignInEffect {
    object LoginSuccess : SignInEffect

    data class ShowError(
        val message: UiText,
    ) : SignInEffect
}
