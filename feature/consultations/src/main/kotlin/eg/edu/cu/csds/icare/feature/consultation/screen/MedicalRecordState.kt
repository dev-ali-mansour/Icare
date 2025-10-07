package eg.edu.cu.csds.icare.feature.consultation.screen

import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord

data class MedicalRecordState(
    val isLoading: Boolean = false,
    val medicalRecord: MedicalRecord? = null,
    val effect: MedicalRecordEffect? = null,
)
