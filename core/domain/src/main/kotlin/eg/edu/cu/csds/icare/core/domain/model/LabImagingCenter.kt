package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LabImagingCenter(
    val id: Long = 0,
    val name: String = "",
    val type: Short = 0,
    val phone: String = "",
    val address: String = "",
)
