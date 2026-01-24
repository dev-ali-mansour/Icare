package eg.edu.cu.csds.icare.feature.appointment.screen.appointments

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class AppointmentListState(
    val isLoading: Boolean = false,
    val appointments: List<Appointment> = emptyList(),
    val effect: AppointmentListEffect? = null,
)

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

sealed interface AppointmentListIntent {
    object OnBackClick : AppointmentListIntent

    object Refresh : AppointmentListIntent

    data class RescheduleAppointment(
        val appointment: Appointment,
    ) : AppointmentListIntent

    data class CancelAppointment(
        val appointment: Appointment,
    ) : AppointmentListIntent

    object ConsumeEffect : AppointmentListIntent
}
