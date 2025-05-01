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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.ui.MainViewModel
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.core.ui.view.BottomBarNavigation
import eg.edu.cu.csds.icare.home.HomeViewModel
import eg.edu.cu.csds.icare.navigation.SetupNavGraph
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    mediaHelper: MediaHelper,
) {
    val layoutDirection = LocalLayoutDirection.current
    val navController = rememberNavController()
    val firebaseAuth: FirebaseAuth by inject(FirebaseAuth::class.java)
    val authViewModel: AuthViewModel = koinViewModel()
    val homeViewModel: HomeViewModel = koinViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomNavItems =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Notifications,
            BottomNavItem.Profile,
            BottomNavItem.Settings,
        )
    val isBottomBarEnabled =
        bottomNavItems.any { item ->
            currentDestination?.hasRoute(item.screen::class) == true
        }
    Scaffold(
        bottomBar = {
            if (isBottomBarEnabled) {
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
                firebaseAuth = firebaseAuth,
                mediaHelper = mediaHelper,
                navController = navController,
                mainViewModel = mainViewModel,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
            )
        }
    }
}
