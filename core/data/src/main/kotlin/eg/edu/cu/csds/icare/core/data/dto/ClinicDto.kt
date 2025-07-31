package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClinicDto(
    val token: String = "",
    @SerialName("clinicID") val id: Long = 0,
    @SerialName("clinicName") val name: String = "",
    @SerialName("clinicType") val type: String = "",
    val phone: String = "",
    @SerialName("clinicLocaltion") val address: String = "",
    val isOpen: Boolean = false,
)
