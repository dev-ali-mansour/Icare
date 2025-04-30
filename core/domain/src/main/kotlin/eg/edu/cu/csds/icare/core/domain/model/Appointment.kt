package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Appointment(
    val id: Long = 0,
    val token: String = "",
    val appointmentId: Long = 0,
    val doctorSpecialty: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val doctorImage: String = "",
    val clinicName: String = "",
    @SerialName("appointmentTime")
    val dateTime: Long = 0,
    val patientId: String = "",
    val patientName: String = "",
    val patientImage: String = "",
    val statusId: Short = 0,
    @Transient
    val status: String = "",
)
