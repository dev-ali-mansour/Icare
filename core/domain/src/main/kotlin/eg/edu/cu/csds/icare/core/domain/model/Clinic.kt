package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Clinic(
    val id: Long = 0,
    val name: String = "",
    val type: String = "",
    val phone: String = "",
    val address: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val isOpen: Boolean = false,
)
