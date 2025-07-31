package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DoctorDto(
    val token: String = "",
    @SerialName("doctorID") val id: String = "",
    @SerialName("fname") val firstName: String = "",
    @SerialName("lname") val lastName: String = "",
    val clinicId: Long = 0,
    val email: String = "",
    @SerialName("phoneNumber") val phone: String = "",
    @SerialName("specialization") val specialty: String = "",
    val fromTime: Long = System.currentTimeMillis(),
    val toTime: Long = fromTime.plus(other = 5 * 60 * 60 * 1000),
    val rating: Double = 0.0,
    val price: Double = 0.0,
    val profilePicture: String = "",
)
