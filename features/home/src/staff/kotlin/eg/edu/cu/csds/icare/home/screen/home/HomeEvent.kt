package eg.edu.cu.csds.icare.home.screen.home

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface HomeEvent {
    object NavigateToProfileScreen : HomeEvent

    object Refresh : HomeEvent

    data class UpdateOpenDialog(
        val isOpen: Boolean,
    ) : HomeEvent

    object NavigateToAppointmentsScreen : HomeEvent

    data class ConfirmAppointment(
        val appointment: Appointment,
    ) : HomeEvent

    data class NavigateToUpdateDoctorScreen(
        val doctor: Doctor,
    ) : HomeEvent

    data class NavigateToNewConsultation(
        val appointment: Appointment,
    ) : HomeEvent

    object NavigateToSectionsAdminScreen : HomeEvent

    object ConsumeEffect : HomeEvent
}
