package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun BottomBarNavigation(
    navController: NavController,
    items: List<BottomNavItem>,
) {
    NavigationBar(
        containerColor = barBackgroundColor,
        contentColor = contentColor,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.iconResId),
                        contentDescription = stringResource(id = item.titleResId),
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.titleResId),
                        fontFamily = helveticaFamily,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                colors =
                    NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = Color.Unspecified,
                        selectedIconColor = Yellow500,
                        selectedTextColor = Yellow500,
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                    ),
                alwaysShowLabel = false,
                selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomNavItemPreview() {
    val bottomNavItems =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Notifications,
            BottomNavItem.Profile,
            BottomNavItem.Settings,
        )
    BottomBarNavigation(navController = rememberNavController(), items = bottomNavItems)
}
