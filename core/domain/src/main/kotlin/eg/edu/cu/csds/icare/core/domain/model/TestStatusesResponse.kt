package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TestStatusesResponse(
    val statusCode: Short,
    val statuses: List<TestStatus>,
)
