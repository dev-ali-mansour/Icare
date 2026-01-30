package eg.edu.cu.csds.icare.feature.onboarding.screen

import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.UiText

data class OnBoardingState(
    val isLoading: Boolean = false,
    val isOnBoardingCompleted: Boolean = false,
    val effect: OnBoardingEffect? = null,
)

sealed interface OnBoardingEffect {
    data object OnBoardingFinished : OnBoardingEffect

    data class NavigateToRoute(
        val route: Route,
    ) : OnBoardingEffect

    data class ShowError(
        val message: UiText,
    ) : OnBoardingEffect
}

sealed interface OnBoardingIntent {
    object FinishOnBoarding : OnBoardingIntent

    object ConsumeEffect : OnBoardingIntent
}
