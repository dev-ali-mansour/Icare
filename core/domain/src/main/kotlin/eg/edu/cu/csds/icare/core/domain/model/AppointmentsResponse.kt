package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentsResponse(
    @SerialName("status")
    val statusCode: Short,
    @SerialName("data")
    val appointments: List<Appointment>,
)
