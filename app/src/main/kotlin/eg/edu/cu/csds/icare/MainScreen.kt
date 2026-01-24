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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.rememberNavBackStack
import eg.edu.cu.csds.icare.core.ui.common.BOTTOM_NAV_ENTRIES
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.navigation.Navigator
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.view.BottomBarNavigation
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingEffect
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingIntent
import eg.edu.cu.csds.icare.feature.onboarding.screen.OnBoardingViewModel
import eg.edu.cu.csds.icare.navigation.NavGraph
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun MainScreen(onBoardingViewModel: OnBoardingViewModel) {
    val backStack = rememberNavBackStack(Route.Splash)
    val navigator = remember { Navigator(backStack) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var isBottomBarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(navigator.currentScreen) {
        delay(timeMillis = 50L)
        isBottomBarVisible =
            BOTTOM_NAV_ENTRIES.any {
                it.key == navigator.currentScreen
            }
    }

    LaunchedUiEffectHandler(
        onBoardingViewModel.effect,
        onConsumeEffect = { onBoardingViewModel.handleIntent(OnBoardingIntent.ConsumeEffect) },
        onEffect = { effect ->
            runCatching {
                when (effect) {
                    is OnBoardingEffect.NavigateToRoute -> {
                        navigator.navigate(effect.route, inclusive = true)
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
                    items = BOTTOM_NAV_ENTRIES,
                    selectedKey = navigator.currentScreen,
                    onSelect = {
                        navigator.navigate(it)
                    },
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
