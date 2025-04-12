package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ExternalPlatform(
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val integrationStatusId: Short = 0,
    @Transient
    val integrationStatus: String = "",
)
