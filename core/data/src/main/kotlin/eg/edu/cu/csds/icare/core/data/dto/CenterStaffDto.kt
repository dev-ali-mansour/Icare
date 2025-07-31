package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CenterStaffDto(
    val token: String = "",
    @SerialName("staffID") val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    @SerialName("labCenterID") val centerId: Long = 0,
    val centerName: String = "",
    val email: String = "",
    @SerialName("phoneNumber") val phone: String = "",
    val profilePicture: String = "",
)
