package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentDto(
    val id: Long = 0,
    val token: String = "",
    val appointmentId: Long = 0,
    val doctorSpecialty: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val doctorImage: String = "",
    val clinicName: String = "",
    @SerialName("appointmentTime") val dateTime: Long = 0,
    val patientId: String = "",
    val patientName: String = "",
    val patientImage: String = "",
    val statusId: Short = 0,
)
