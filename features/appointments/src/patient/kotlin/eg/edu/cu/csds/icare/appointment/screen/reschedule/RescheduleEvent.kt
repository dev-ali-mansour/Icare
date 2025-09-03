package eg.edu.cu.csds.icare.appointment.screen.reschedule

import eg.edu.cu.csds.icare.core.domain.model.Appointment

sealed interface RescheduleEvent {
    object Refresh : RescheduleEvent

    data class SelectAppointment(
        val appointment: Appointment,
    ) : RescheduleEvent

    data class UpdateSelectedSlot(
        val slot: Long,
    ) : RescheduleEvent

    object Proceed : RescheduleEvent

    object ConsumeEffect : RescheduleEvent
}
