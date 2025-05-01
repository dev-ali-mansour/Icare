package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class MedicalRecord(
    val patientId: String = "",
    val patientName: String = "",
    val patientImage: String = "",
    val gender: Char = 'M',
    @Transient
    val genderValue: String = "",
    val chronicDiseases: String = "",
    val currentMedications: String = "",
    val allergies: String = "",
    val pastSurgeries: String = "",
    val weight: Double = 0.0,
    val consultations: List<Consultation> = emptyList(),
)
