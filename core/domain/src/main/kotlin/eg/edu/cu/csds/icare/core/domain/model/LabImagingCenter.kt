package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LabImagingCenter(
    @Transient
    val token: String = "",
    @SerialName("centerID")
    val id: Long = 0,
    @SerialName("centerName")
    val name: String = "",
    @SerialName("centerType")
    val type: Short = 0,
    val phone: String = "",
    val address: String = "",
)
