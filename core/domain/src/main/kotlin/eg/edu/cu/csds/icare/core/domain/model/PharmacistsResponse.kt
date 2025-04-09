package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PharmacistsResponse(
    val statusCode: Short,
    val pharmacists: List<Pharmacist>,
)
