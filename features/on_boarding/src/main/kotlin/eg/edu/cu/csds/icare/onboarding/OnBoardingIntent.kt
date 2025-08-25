package eg.edu.cu.csds.icare.onboarding

sealed interface OnBoardingIntent {
    object FinishOnBoarding : OnBoardingIntent
}
