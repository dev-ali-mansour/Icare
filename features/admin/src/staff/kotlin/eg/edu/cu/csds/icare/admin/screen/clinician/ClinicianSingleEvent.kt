package eg.edu.cu.csds.icare.admin.screen.clinician

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicianSingleEvent {
    object ShowSuccess : ClinicianSingleEvent

    data class ShowError(
        val message: UiText,
    ) : ClinicianSingleEvent
}
