package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DoctorSchedule(
    val totalPatients: Long = 0,
    val confirmed: Long = 0,
    val price: Double = 0.0,
    val availableSlots: Short = 0,
    val appointments: List<Appointment>,
)
