package eg.edu.cu.csds.icare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.compose.rememberNavController
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.BottomBarNavigation
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.navigation.SetupNavGraph
import eg.edu.cu.csds.icare.splash.SplashSingleEvent
import eg.edu.cu.csds.icare.splash.SplashViewModel
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.system.exitProcess

@Composable
fun MainScreen(splashViewModel: SplashViewModel) {
    val navController = rememberNavController()
    var isBottomBarVisible by remember { mutableStateOf(false) }
    val layoutDirection = LocalLayoutDirection.current
    val context = LocalContext.current
    rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    val bottomNavItems =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Notifications,
            BottomNavItem.Profile,
            BottomNavItem.Settings,
        )

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            isBottomBarVisible = destination.route != Route.Splash::class.qualifiedName &&
                bottomNavItems.any { it.route::class.qualifiedName == destination.route }
        }
    }

    LaunchedEffect(navController, splashViewModel) {
        splashViewModel.singleEvent.collect { event ->
            runCatching {
                val currentStartDestinationId = navController.graph.startDestinationId
                val popUpToRoute = navController.graph.findNode(currentStartDestinationId)?.route

                val navigateAndPopUp: (Route) -> Unit = { screen ->
                    navController.navigate(screen) {
                        if (popUpToRoute != null) {
                            popUpTo(popUpToRoute) { inclusive = true }
                        } else {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                }

                when (event) {
                    is SplashSingleEvent.NavigateToSignIn -> navigateAndPopUp(Route.SignIn)
                    is SplashSingleEvent.NavigateToHome -> navigateAndPopUp(Route.Home)
                    is SplashSingleEvent.NavigateToOnBoarding -> navigateAndPopUp(Route.OnBoarding)
                    is SplashSingleEvent.ShowError -> {
                        alertMessage = event.message.asString(context)
                        showAlert = true
                        delay(timeMillis = 5000)
                        showAlert = false
                        exitProcess(0)
                    }
                }
            }.onFailure {
                Timber.e(it, "Error during navigation in MainScreen")
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                BottomBarNavigation(
                    navController = navController,
                    items = bottomNavItems,
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .background(color = backgroundColor)
                    .fillMaxSize()
                    .padding(
                        start = paddingValues.calculateStartPadding(layoutDirection),
                        end = paddingValues.calculateEndPadding(layoutDirection),
                        bottom = paddingValues.calculateBottomPadding(),
                    ),
        ) {
            SetupNavGraph(navController = navController)

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}
