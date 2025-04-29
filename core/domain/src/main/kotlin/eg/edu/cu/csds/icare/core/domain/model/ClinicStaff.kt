package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ClinicStaff(
    val token: String = "",
    val id: String = "",
    @SerialName("fname")
    val firstName: String = "",
    @SerialName("lname")
    val lastName: String = "",
    @Transient
    val name: String = "$firstName $lastName",
    val clinicId: Long = 0,
    val clinicName: String = "",
    val email: String = "",
    @SerialName("phoneNumber")
    val phone: String = "",
    val profilePicture: String = "",
)
