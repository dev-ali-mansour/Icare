package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pharmacy(
    val token: String = "",
    @SerialName("pharmacyID")
    val id: Long = 0,
    @SerialName("pharmacyName")
    val name: String = "",
    val phone: String = "",
    @SerialName("pharmacyAddress")
    val address: String = "",
)
