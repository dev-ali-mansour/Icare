package eg.edu.cu.csds.icare.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.onboarding.screen.OnBoardingScreen

fun NavGraphBuilder.onBoardingRoute(onCompleted: () -> Unit) {
    composable<Screen.OnBoarding> {
        OnBoardingScreen(onCompleted = { onCompleted() })
    }
}
