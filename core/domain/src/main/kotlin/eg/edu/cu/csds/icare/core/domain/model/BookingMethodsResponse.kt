package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BookingMethodsResponse(
    val statusCode: Short,
    val methods: List<BookingMethod>,
)
