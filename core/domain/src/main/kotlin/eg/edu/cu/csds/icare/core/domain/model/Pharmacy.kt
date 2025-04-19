package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Pharmacy(
    @Transient
    val token: String = "",
    @SerialName("pharmacyID")
    val id: Long = 0,
    @SerialName("pharmacyName")
    val name: String = "",
    val phone: String = "",
    @SerialName("pharmacyAddress")
    val address: String = "",
)
