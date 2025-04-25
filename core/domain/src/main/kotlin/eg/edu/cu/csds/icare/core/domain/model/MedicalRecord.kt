package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MedicalRecord(
    val token: String = "",
    val patientId: String = "",
    val patientName: String = "",
    val patientImage: String = "",
    val consultations: List<Consultation>,
)
