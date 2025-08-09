package eg.edu.cu.csds.icare.auth.screen.profile

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ProfileSingleEvent {
    object SignOutSuccess : ProfileSingleEvent

    data class ShowError(
        val message: UiText,
    ) : ProfileSingleEvent
}
