package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DoctorScheduleDto(
    val totalPatients: Long = 0,
    val confirmed: Long = 0,
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val fromTime: Long = System.currentTimeMillis(),
    val toTime: Long = System.currentTimeMillis().plus(other = 5 * 60 * 60 * 60 * 1000),
    val availableSlots: Short = 0,
    val appointments: List<AppointmentDto>,
)
