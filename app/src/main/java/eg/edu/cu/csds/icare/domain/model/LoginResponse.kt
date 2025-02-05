package eg.edu.cu.csds.icare.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val statusCode: Short = 0,
    val user: User = User(),
)
