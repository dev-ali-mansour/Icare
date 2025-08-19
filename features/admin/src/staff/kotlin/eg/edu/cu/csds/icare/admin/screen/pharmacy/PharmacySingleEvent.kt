package eg.edu.cu.csds.icare.admin.screen.pharmacy

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PharmacySingleEvent {
    object ShowSuccess : PharmacySingleEvent

    data class ShowError(
        val message: UiText,
    ) : PharmacySingleEvent
}
