package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginInfoResponse(
    val statusCode: Short,
    val role: UserRole?,
    val job: String = "",
)
