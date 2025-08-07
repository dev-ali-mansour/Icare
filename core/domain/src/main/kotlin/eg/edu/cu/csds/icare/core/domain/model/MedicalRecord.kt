package eg.edu.cu.csds.icare.core.domain.model

data class MedicalRecord(
    val patientId: String = "",
    val patientName: String = "",
    val patientImage: String = "",
    val gender: Char = 'M',
    val genderValue: String = "",
    val chronicDiseases: String = "",
    val currentMedications: String = "",
    val allergies: String = "",
    val pastSurgeries: String = "",
    val weight: Double = 0.0,
    val consultations: List<Consultation> = emptyList(),
)
