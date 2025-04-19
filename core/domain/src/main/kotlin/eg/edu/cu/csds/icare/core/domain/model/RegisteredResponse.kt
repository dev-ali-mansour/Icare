package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisteredResponse(
    @SerialName("status")
    val statusCode: Short,
    @SerialName("registered")
    val isRegistered: Boolean,
)
