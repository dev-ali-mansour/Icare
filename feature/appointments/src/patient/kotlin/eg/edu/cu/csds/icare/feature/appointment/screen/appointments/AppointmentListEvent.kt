package eg.edu.cu.csds.icare.feature.appointment.screen.appointments

import eg.edu.cu.csds.icare.core.domain.model.Appointment

sealed interface AppointmentListEvent {
    object OnBackClick : AppointmentListEvent

    object Refresh : AppointmentListEvent

    data class RescheduleAppointment(
        val appointment: Appointment,
    ) : AppointmentListEvent

    data class CancelAppointment(
        val appointment: Appointment,
    ) : AppointmentListEvent

    object ConsumeEffect : AppointmentListEvent
}
