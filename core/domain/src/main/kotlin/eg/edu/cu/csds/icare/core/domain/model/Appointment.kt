package eg.edu.cu.csds.icare.core.domain.model


import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Appointment (
    val appointmentId: Short,
    val doctorName: String,
    val doctorSpecialty: String,
    val doctorImage: Int,// Resource ID for the image
    val time: String,
    val date: String,
    val status: AppointmentStatus
    )