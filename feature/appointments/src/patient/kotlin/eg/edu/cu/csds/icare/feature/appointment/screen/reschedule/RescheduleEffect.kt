package eg.edu.cu.csds.icare.feature.appointment.screen.reschedule

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface RescheduleEffect {
    object ShowSuccess : RescheduleEffect

    data class ShowError(
        val message: UiText,
    ) : RescheduleEffect
}
