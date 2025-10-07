package eg.edu.cu.csds.icare.feature.auth.screen.profile

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ProfileEffect {
    object SignOutSuccess : ProfileEffect

    data class ShowError(
        val message: UiText,
    ) : ProfileEffect
}
