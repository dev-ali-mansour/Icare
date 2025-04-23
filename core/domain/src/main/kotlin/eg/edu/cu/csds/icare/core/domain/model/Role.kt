package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val token: String = "",
    val id: Short = 0,
    val name: String = "",
    val permissions: List<Short> = listOf(),
)
