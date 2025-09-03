package eg.edu.cu.csds.icare.consultation.screen

import eg.edu.cu.csds.icare.core.domain.model.Consultation

sealed interface MedicalRecordEvent {
    object Refresh : MedicalRecordEvent

    data class NavigateToConsultation(
        val consultation: Consultation,
    ) : MedicalRecordEvent

    data class LoadMedicalRecord(
        val patientId: String,
    ) : MedicalRecordEvent

    object ConsumeEffect : MedicalRecordEvent
}
