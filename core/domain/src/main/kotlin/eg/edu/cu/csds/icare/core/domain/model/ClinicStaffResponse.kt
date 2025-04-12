package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ClinicStaffResponse(
    val statusCode: Short,
    val staffList: List<ClinicStaff>,
)
