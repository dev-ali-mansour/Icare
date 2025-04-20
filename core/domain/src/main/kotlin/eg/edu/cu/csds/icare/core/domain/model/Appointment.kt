package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Appointment(
    @Transient
    val token: String = "",
    val appointmentId: Long = 0,
    val doctorSpecialty: String = "",
    val doctorId: String = "",
    @Transient
    val doctorName: String = "",
    val doctorImage: String = "",
    val dateTime: Long = 0,
    val patientName: String = "",
    val patientImage: String = "",
    val statusId: Short = 0,
    @Transient
    val status: String = "",
)
