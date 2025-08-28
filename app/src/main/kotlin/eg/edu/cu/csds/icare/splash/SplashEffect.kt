package eg.edu.cu.csds.icare.splash

import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface SplashEffect {
    data class NavigateToRoute(
        val route: Route,
    ) : SplashEffect

    data class ShowError(
        val message: UiText,
    ) : SplashEffect
}
