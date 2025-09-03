package eg.edu.cu.csds.icare.appointment.screen.booking

import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule

data class BookingState(
    val isLoading: Boolean = false,
    val doctor: Doctor? = null,
    val doctorSchedule: DoctorSchedule? = null,
    val selectedSlot: Long = 0L,
    val effect: BookingEffect? = null,
)
