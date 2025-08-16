package eg.edu.cu.csds.icare.admin.screen.clinic.doctor

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface DoctorSingleEvent {
    object ShowSuccess : DoctorSingleEvent

    data class ShowError(
        val message: UiText,
    ) : DoctorSingleEvent
}
