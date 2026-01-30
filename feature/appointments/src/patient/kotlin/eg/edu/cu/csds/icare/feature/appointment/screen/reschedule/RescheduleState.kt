package eg.edu.cu.csds.icare.feature.appointment.screen.reschedule

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class RescheduleState(
    val isLoading: Boolean = false,
    val appointment: Appointment? = null,
    val doctorSchedule: DoctorSchedule? = null,
    val effect: RescheduleEffect? = null,
)

sealed interface RescheduleEffect {
    object ShowSuccess : RescheduleEffect

    data class ShowError(
        val message: UiText,
    ) : RescheduleEffect
}

sealed interface RescheduleIntent {
    object Refresh : RescheduleIntent

    data class SelectAppointment(
        val appointment: Appointment,
    ) : RescheduleIntent

    data class UpdateSelectedSlot(
        val slot: Long,
    ) : RescheduleIntent

    object Proceed : RescheduleIntent

    object ConsumeEffect : RescheduleIntent
}
