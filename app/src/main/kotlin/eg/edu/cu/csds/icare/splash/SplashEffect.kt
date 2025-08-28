package eg.edu.cu.csds.icare.splash

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface SplashEffect {
    object NavigateToHome : SplashEffect

    object NavigateToSignIn : SplashEffect

    object NavigateToOnBoarding : SplashEffect

    data class ShowError(
        val message: UiText,
    ) : SplashEffect
}
