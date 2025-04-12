package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Pharmacy(
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val contractStatusId: Short = 0,
    @Transient
    val contractStatus: String = "",
)
