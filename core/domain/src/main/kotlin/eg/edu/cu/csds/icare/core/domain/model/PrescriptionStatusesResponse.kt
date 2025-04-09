package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionStatusesResponse(
    val statusCode: Short,
    val statuses: List<PrescriptionStatus>,
)
