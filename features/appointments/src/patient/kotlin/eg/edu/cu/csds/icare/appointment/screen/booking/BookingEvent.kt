package eg.edu.cu.csds.icare.appointment.screen.booking

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface BookingEvent {
    object Refresh : BookingEvent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : BookingEvent

    data class UpdateSelectedSlot(
        val slot: Long,
    ) : BookingEvent

    object Proceed : BookingEvent

    object ConsumeEffect : BookingEvent
}
