package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentsResponse(
    val statusCode: Short,
    val appointments: List<Appointment>,
)
