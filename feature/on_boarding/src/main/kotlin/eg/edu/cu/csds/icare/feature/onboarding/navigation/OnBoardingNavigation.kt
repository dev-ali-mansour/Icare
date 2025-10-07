package eg.edu.cu.csds.icare.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingScreen

fun NavGraphBuilder.onBoardingRoute(onFinished: () -> Unit) {
    composable<Route.OnBoarding> {
        OnBoardingScreen(onFinished = { onFinished() })
    }
}
