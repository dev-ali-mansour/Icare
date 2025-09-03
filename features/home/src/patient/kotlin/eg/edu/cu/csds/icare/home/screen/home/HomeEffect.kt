package eg.edu.cu.csds.icare.home.screen.home

import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface HomeEffect {
    data class NavigateToRoute(
        val route: Route,
    ) : HomeEffect

    data class NavigateToDoctorDetails(
        val doctor: Doctor,
    ) : HomeEffect

    data class ShowError(
        val message: UiText,
    ) : HomeEffect
}
