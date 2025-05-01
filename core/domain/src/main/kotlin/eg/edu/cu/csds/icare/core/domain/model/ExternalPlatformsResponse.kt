package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExternalPlatformsResponse(
    val statusCode: Short,
    val platforms: List<ExternalPlatform>,
)
