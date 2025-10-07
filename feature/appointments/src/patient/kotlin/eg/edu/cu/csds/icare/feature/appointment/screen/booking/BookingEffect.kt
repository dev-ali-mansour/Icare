package eg.edu.cu.csds.icare.feature.appointment.screen.booking

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface BookingEffect {
    object ShowSuccess : BookingEffect

    data class ShowError(
        val message: UiText,
    ) : BookingEffect
}
