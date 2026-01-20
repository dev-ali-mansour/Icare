package eg.edu.cu.csds.icare

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.navigation.Navigator
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.view.BottomBarNavigation
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingEffect
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingIntent
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingViewModel
import eg.edu.cu.csds.icare.navigation.NavGraph
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import timber.log.Timber
import kotlin.system.exitProcess

@Composable
fun MainScreen(onBoardingViewModel: OnBoardingViewModel) {
    val navigator: Navigator = koinInject()
    val context = LocalContext.current
    var alertMessage by retain { mutableStateOf("") }
    var showAlert by retain { mutableStateOf(false) }
    val bottomNavItems =
        retain {
            listOf(
                BottomNavItem.Home,
                BottomNavItem.Notifications,
                BottomNavItem.Profile,
                BottomNavItem.Settings,
            )
        }

    val isBottomBarVisible by retain {
        derivedStateOf {
            val currentDestination = navigator.backStack.lastOrNull()
            navigator.backStack.isNotEmpty() && bottomNavItems.any { it.route == currentDestination }
        }
    }

    LaunchedUiEffectHandler(
        onBoardingViewModel.effect,
        onConsumeEffect = { onBoardingViewModel.handleIntent(OnBoardingIntent.ConsumeEffect) },
        onEffect = { effect ->
            runCatching {
                when (effect) {
                    is OnBoardingEffect.NavigateToRoute -> {
                        navigator.goTo(effect.route)
                        if (navigator.backStack.contains(Route.Splash)) {
                            navigator.backStack.remove(Route.Splash)
                        }
                    }

                    is OnBoardingEffect.OnBoardingFinished -> {}

                    is OnBoardingEffect.ShowError -> {
                        alertMessage = effect.message.asString(context)
                        showAlert = true
                        delay(timeMillis = 5000)
                        showAlert = false
                        exitProcess(0)
                    }
                }
            }.onFailure {
                Timber.e(it, "Error during navigation in MainScreen")
            }
        },
    )

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                BottomBarNavigation(
                    backStack = navigator.backStack,
                    onNavigate = { route ->
                        if (navigator.backStack.lastOrNull() != route) {
                            navigator.goTo(route)
                        }
                    },
                    items = bottomNavItems,
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        NavGraph(
            navigator = navigator,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
        )

        if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
    }
}
