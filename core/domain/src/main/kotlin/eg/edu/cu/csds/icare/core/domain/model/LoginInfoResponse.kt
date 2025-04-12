package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginInfoResponse(
    val statusCode: Short,
    val role: Role?,
    val job: String = "",
)
