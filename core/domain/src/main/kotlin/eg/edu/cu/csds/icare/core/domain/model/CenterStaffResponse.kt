package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CenterStaffResponse(
    val statusCode: Short,
    val staffList: List<CenterStaff>,
)
