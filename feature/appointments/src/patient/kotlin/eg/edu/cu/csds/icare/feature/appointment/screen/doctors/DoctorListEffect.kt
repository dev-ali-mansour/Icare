package eg.edu.cu.csds.icare.feature.appointment.screen.doctors

import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface DoctorListEffect {
    object OnBackClick : DoctorListEffect

    data class NavigateToBookingRoute(
        val doctor: Doctor,
    ) : DoctorListEffect

    data class ShowError(
        val message: UiText,
    ) : DoctorListEffect
}
