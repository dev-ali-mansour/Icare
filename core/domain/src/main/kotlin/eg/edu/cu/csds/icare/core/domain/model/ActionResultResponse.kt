package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActionResultResponse(
    @SerialName("code")
    val statusCode: String = "",
    @SerialName("message")
    val message: String = "",
)
