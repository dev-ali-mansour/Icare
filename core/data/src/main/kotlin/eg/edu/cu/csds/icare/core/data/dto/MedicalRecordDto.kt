package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MedicalRecordDto(
    val patientId: String = "",
    val patientName: String = "",
    val patientImage: String = "",
    val gender: Char = 'M',
    val chronicDiseases: String = "",
    val currentMedications: String = "",
    val allergies: String = "",
    val pastSurgeries: String = "",
    val weight: Double = 0.0,
    val consultations: List<ConsultationDto> = emptyList(),
)
