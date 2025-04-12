package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ClinicsResponse(
    val statusCode: Short,
    val clinics: List<Clinic>,
)
