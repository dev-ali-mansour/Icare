package eg.edu.cu.csds.icare.feature.onboarding.screen

data class OnBoardingState(
    val isLoading: Boolean = false,
    val isOnBoardingCompleted: Boolean = false,
    val effect: OnBoardingEffect? = null,
)
