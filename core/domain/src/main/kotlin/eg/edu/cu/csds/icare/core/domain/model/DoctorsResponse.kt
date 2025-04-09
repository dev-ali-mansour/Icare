package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DoctorsResponse(
    val statusCode: Short,
    val doctors: List<Doctor>,
)
