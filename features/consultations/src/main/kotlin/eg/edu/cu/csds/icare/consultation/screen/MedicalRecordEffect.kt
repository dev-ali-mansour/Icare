package eg.edu.cu.csds.icare.consultation.screen

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface MedicalRecordEffect {
    data class NavigateToConsultation(
        val consultation: Consultation,
    ) : MedicalRecordEffect

    data class ShowError(
        val message: UiText,
    ) : MedicalRecordEffect
}
