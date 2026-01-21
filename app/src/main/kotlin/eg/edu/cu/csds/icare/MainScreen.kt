package eg.edu.cu.csds.icare

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.navigation.Navigator
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.view.BottomBarNavigation
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingEffect
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingIntent
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingViewModel
import eg.edu.cu.csds.icare.navigation.NavGraph
import org.koin.compose.koinInject
import timber.log.Timber

@Composable
fun MainScreen(onBoardingViewModel: OnBoardingViewModel) {
    val navigator: Navigator = koinInject()
    val context = LocalContext.current
    val snackbarHostState = retain { SnackbarHostState() }

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
                        snackbarHostState.showSnackbar(
                            message = effect.message.asString(context),
                            duration = SnackbarDuration.Long,
                        )
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        NavGraph(
            navigator = navigator,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
        )
    }
}
