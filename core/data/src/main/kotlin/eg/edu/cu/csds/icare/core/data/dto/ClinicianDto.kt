package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClinicianDto(
    val token: String = "",
    val id: String = "",
    @SerialName("fname") val firstName: String = "",
    @SerialName("lname") val lastName: String = "",
    val clinicId: Long = 0,
    val clinicName: String = "",
    val email: String = "",
    @SerialName("phoneNumber") val phone: String = "",
    val profilePicture: String = "",
)
