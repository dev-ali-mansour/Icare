package eg.edu.cu.csds.icare.feature.appointment.screen.booking

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class BookingState(
    val isLoading: Boolean = false,
    val doctor: Doctor? = null,
    val doctorSchedule: DoctorSchedule? = null,
    val selectedSlot: Long = 0L,
    val effect: BookingEffect? = null,
)

sealed interface BookingEffect {
    object ShowSuccess : BookingEffect

    data class ShowError(
        val message: UiText,
    ) : BookingEffect
}

sealed interface BookingIntent {
    object Refresh : BookingIntent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : BookingIntent

    data class UpdateSelectedSlot(
        val slot: Long,
    ) : BookingIntent

    object Proceed : BookingIntent

    object ConsumeEffect : BookingIntent
}
