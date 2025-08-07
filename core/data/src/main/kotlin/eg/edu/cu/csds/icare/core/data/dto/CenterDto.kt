package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CenterDto(
    val token: String = "",
    @SerialName("centerID") val id: Long = 0,
    @SerialName("centerName") val name: String = "",
    @SerialName("centerType") val type: Short = 0,
    val phone: String = "",
    val address: String = "",
)
