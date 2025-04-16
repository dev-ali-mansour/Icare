package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CenterStaff(
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    @Transient
    val name: String = "$firstName $lastName",
    val centerId: Long = 0,
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
)
