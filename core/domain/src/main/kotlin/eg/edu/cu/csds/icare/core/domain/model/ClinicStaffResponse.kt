package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClinicStaffResponse(
    @SerialName("status")
    val statusCode: Short,
    val staffList: List<ClinicStaff>,
)
