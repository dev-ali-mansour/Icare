package eg.edu.cu.csds.icare.feature.onboarding.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingScreen

fun EntryProviderScope<NavKey>.onBoardingEntryBuilder(onFinished: () -> Unit) {
    entry<Route.OnBoarding> {
        OnBoardingScreen(onFinished = { onFinished() })
    }
}
