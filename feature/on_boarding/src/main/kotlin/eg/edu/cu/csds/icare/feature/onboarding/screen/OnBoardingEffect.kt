package eg.edu.cu.csds.icare.feature.onboarding.screen

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface OnBoardingEffect {
    data object OnBoardingFinished : OnBoardingEffect

    data class ShowError(
        val message: UiText,
    ) : OnBoardingEffect
}
