package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsultationsResponse(
    @SerialName("status")
    val statusCode: Short,
    @SerialName("data")
    val consultations: List<Consultation>,
)
