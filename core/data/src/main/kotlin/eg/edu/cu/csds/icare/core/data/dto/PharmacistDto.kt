package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PharmacistDto(
    val token: String = "",
    @SerialName("pharmacistID") val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val pharmacyId: Long = 0,
    val pharmacyName: String = "",
    val email: String = "",
    @SerialName("phoneNumber") val phone: String = "",
    val profilePicture: String = "",
)
