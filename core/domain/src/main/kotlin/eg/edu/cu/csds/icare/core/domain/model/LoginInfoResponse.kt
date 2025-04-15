package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginInfoResponse(
    @SerialName("status")
    val statusCode: Short,
    val role: Role?,
    val job: String,
)
