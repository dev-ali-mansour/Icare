package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PharmaciesResponse(
    val statusCode: Short,
    val pharmacies: List<Pharmacy>,
)
