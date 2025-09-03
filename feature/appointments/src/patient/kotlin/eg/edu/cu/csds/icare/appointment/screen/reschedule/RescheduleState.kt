package eg.edu.cu.csds.icare.appointment.screen.reschedule

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule

data class RescheduleState(
    val isLoading: Boolean = false,
    val appointment: Appointment? = null,
    val doctorSchedule: DoctorSchedule? = null,
    val effect: RescheduleEffect? = null,
)
