package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRole(
    val roleId: Short,
    val permissions: List<Short> = listOf(),
)
