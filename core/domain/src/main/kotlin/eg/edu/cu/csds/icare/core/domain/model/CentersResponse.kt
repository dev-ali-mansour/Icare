package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CentersResponse(
    val statusCode: Short,
    val centers: List<LabImagingCenter>,
)
