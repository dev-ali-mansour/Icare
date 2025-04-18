package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Pharmacy(
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
)
