package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RolesResponse(
    val statusCode: Short,
    val roles: List<Role>,
)
