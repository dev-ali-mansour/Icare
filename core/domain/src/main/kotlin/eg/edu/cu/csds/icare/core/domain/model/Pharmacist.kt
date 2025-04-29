package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Pharmacist(
    val token: String = "",
    @SerialName("pharmacistID")
    val id: String = "",
    @SerialName("fname")
    val firstName: String = "",
    @SerialName("lname")
    val lastName: String = "",
    @Transient
    val name: String = "$firstName $lastName",
    val pharmacyId: Long = 0,
    val pharmacyName: String = "",
    val email: String = "",
    @SerialName("phoneNumber")
    val phone: String = "",
    val profilePicture: String = "",
)
