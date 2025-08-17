package eg.edu.cu.csds.icare.admin.screen.clinic

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicSingleEvent {
    object ShowSuccess : ClinicSingleEvent

    data class ShowError(
        val message: UiText,
    ) : ClinicSingleEvent
}
