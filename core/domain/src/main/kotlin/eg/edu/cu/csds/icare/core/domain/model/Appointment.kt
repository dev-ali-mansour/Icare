package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Appointment(
    @Transient
    val token: String = "",
    val appointmentId: Long,
    val doctorName: String,
    val doctorSpecialty: String,
    val doctorImage: String,
    val dateTime: Long,
    val patientName: String,
    val patientImage: String,
    val statusId: Short,
    @Transient
    val status: String = "",
)
