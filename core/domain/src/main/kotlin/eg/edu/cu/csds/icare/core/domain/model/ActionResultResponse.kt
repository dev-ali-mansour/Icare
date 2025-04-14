package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActionResultResponse(
    @SerialName("status")
    val statusCode: Short,
    @SerialName("message")
    val message: String,
)
