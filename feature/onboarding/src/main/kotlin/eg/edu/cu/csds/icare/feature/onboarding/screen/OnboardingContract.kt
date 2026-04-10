package eg.edu.cu.csds.icare.feature.onboarding.screen

import eg.edu.cu.csds.icare.core.ui.common.OnBoardingPage
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.feature.onboarding.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class OnBoardingState(
    val isLoading: Boolean = false,
    val pages: PersistentList<OnBoardingPage> =
        persistentListOf(
            OnBoardingPage(
                image = R.drawable.feature_onboarding_first_page_image,
                title = StringResourceId(R.string.feature_onboarding_first_page_title),
                description = StringResourceId(R.string.feature_onboarding_first_page_description),
            ),
            OnBoardingPage(
                image = R.drawable.feature_onboarding_second_page_image,
                title = StringResourceId(R.string.feature_onboarding_second_page_title),
                description = StringResourceId(R.string.feature_onboarding_second_page_description),
            ),
            OnBoardingPage(
                image = R.drawable.feature_onboarding_first_page_image,
                title = StringResourceId(R.string.feature_onboarding_third_page_title),
                description = StringResourceId(R.string.feature_onboarding_third_page_description),
            ),
        ),
    val isOnBoardingCompleted: Boolean = false,
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
}
