package eg.edu.cu.csds.icare.feature.appointment.screen.appointments

import eg.edu.cu.csds.icare.core.domain.model.Appointment

data class AppointmentListState(
    val isLoading: Boolean = false,
    val appointments: List<Appointment> = emptyList(),
    val effect: AppointmentListEffect? = null,
)
