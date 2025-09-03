package eg.edu.cu.csds.icare.onboarding.screen

sealed interface OnBoardingEvent {
    object FinishOnBoarding : OnBoardingEvent

    object ConsumeEffect : OnBoardingEvent
}
