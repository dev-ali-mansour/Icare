package eg.edu.cu.csds.icare.splash

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface SplashSingleEvent {
    object NavigateToHome : SplashSingleEvent

    object NavigateToSignIn : SplashSingleEvent

    object NavigateToOnBoarding : SplashSingleEvent

    data class ShowError(
        val message: UiText,
    ) : SplashSingleEvent
}
