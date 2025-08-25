package eg.edu.cu.csds.icare.onboarding

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface OnBoardingSingleEvent {
    data object OnBoardingFinished : OnBoardingSingleEvent

    data class ShowError(
        val message: UiText,
    ) : OnBoardingSingleEvent
}
