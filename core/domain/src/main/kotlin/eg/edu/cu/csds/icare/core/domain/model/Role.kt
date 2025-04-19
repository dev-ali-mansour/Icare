package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Role(
    @Transient
    val token: String = "",
    val id: Short = 0,
    val name: String = "",
    val permissions: List<Short> = listOf(),
)
