package eg.edu.cu.csds.icare.feature.consultation.screen

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class MedicalRecordState(
    val isLoading: Boolean = false,
    val medicalRecord: MedicalRecord? = null,
    val effect: MedicalRecordEffect? = null,
)

sealed interface MedicalRecordEffect {
    data class NavigateToConsultation(
        val consultation: Consultation,
    ) : MedicalRecordEffect

    data class ShowError(
        val message: UiText,
    ) : MedicalRecordEffect
}

sealed interface MedicalRecordIntent {
    object Refresh : MedicalRecordIntent

    data class NavigateToConsultation(
        val consultation: Consultation,
    ) : MedicalRecordIntent

    data class LoadMedicalRecord(
        val patientId: String,
    ) : MedicalRecordIntent

    object ConsumeEffect : MedicalRecordIntent
}
