package eg.edu.cu.csds.icare.feature.appointment.screen.appointments

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface AppointmentListEffect {
    object OnBackClick : AppointmentListEffect

    data class NavigateToRescheduleAppointment(
        val appointment: Appointment,
    ) : AppointmentListEffect

    object ShowSuccess : AppointmentListEffect

    data class ShowError(
        val message: UiText,
    ) : AppointmentListEffect
}
