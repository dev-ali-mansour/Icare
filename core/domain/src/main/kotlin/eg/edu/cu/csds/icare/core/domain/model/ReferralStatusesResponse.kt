package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReferralStatusesResponse(
    val statusCode: Short,
    val statuses: List<ReferralStatus>,
)
