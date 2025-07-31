package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CentersResponse(
    @SerialName("status")
    val statusCode: Short,
    @SerialName("data")
    val centers: List<CenterDto>,
)
