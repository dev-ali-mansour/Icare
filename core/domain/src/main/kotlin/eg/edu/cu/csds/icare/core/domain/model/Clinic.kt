package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Clinic(
    @Transient
    val token: String = "",
    @SerialName("clinicID")
    val id: Long = 0,
    @SerialName("clinicName")
    val name: String = "",
    @SerialName("clinicType")
    val type: String = "",
    val phone: String = "",
    @SerialName("clinicLocaltion")
    val address: String = "",
    val isOpen: Boolean = false,
)
