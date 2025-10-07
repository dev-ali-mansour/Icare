package eg.edu.cu.csds.icare.feature.onboarding.screen

sealed interface OnBoardingEvent {
    object FinishOnBoarding : OnBoardingEvent

    object ConsumeEffect : OnBoardingEvent
}
