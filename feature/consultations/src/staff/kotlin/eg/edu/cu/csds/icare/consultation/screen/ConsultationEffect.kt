package eg.edu.cu.csds.icare.consultation.screen

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ConsultationEffect {
    object ShowSuccess : ConsultationEffect

    data class NavigateToMedicalRecord(
        val patientId: String,
    ) : ConsultationEffect

    data class ShowError(
        val message: UiText,
    ) : ConsultationEffect
}
