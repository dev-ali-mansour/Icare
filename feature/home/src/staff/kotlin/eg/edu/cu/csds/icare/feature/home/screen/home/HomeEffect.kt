package eg.edu.cu.csds.icare.feature.home.screen.home

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface HomeEffect {
    data class NavigateToRoute(
        val route: Route,
    ) : HomeEffect

    data class NavigateToUpdateDoctorScreen(
        val doctor: Doctor,
    ) : HomeEffect

    data class NavigateToNewConsultation(
        val appointment: Appointment,
    ) : HomeEffect

    data class ShowError(
        val message: UiText,
    ) : HomeEffect
}
