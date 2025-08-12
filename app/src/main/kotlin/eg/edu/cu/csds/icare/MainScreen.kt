package eg.edu.cu.csds.icare

import android.widget.Toast
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.compose.rememberNavController
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.view.BottomBarNavigation
import eg.edu.cu.csds.icare.navigation.SetupNavGraph
import eg.edu.cu.csds.icare.splash.SplashSingleEvent
import eg.edu.cu.csds.icare.splash.SplashViewModel
import kotlin.system.exitProcess

@Composable
fun MainScreen(splashViewModel: SplashViewModel) {
    val navController = rememberNavController()
    var isBottomBarVisible by remember { mutableStateOf(false) }
    val layoutDirection = LocalLayoutDirection.current
    val context = LocalContext.current
    val bottomNavItems =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Notifications,
            BottomNavItem.Profile,
            BottomNavItem.Settings,
        )

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            isBottomBarVisible = destination.route != Screen.Splash::class.qualifiedName &&
                bottomNavItems.any { it.screen::class.qualifiedName == destination.route }
        }
    }

    LaunchedEffect(navController, splashViewModel) {
        splashViewModel.singleEvent.collect { event ->
            val currentStartDestinationId = navController.graph.startDestinationId
            val popUpToRoute = navController.graph.findNode(currentStartDestinationId)?.route

            val navigateAndPopUp: (Screen) -> Unit = { screen ->
                navController.navigate(screen) {
                    if (popUpToRoute != null) {
                        popUpTo(popUpToRoute) { inclusive = true }
                    } else {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }

            when (event) {
                is SplashSingleEvent.NavigateToSignIn -> navigateAndPopUp(Screen.SignIn)
                is SplashSingleEvent.NavigateToHome -> navigateAndPopUp(Screen.Home)
                is SplashSingleEvent.NavigateToOnBoarding -> navigateAndPopUp(Screen.OnBoarding)
                is SplashSingleEvent.ShowError -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                    exitProcess(0)
                }
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
            SetupNavGraph(
                navController = navController,
            )
        }
    }
}
