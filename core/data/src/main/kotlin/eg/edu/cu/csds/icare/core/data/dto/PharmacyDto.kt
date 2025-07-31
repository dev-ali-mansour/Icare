package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PharmacyDto(
    val token: String = "",
    @SerialName("pharmacyID") val id: Long = 0,
    @SerialName("pharmacyName") val name: String = "",
    val phone: String = "",
    @SerialName("pharmacyAddress")
    val address: String = "",
)
