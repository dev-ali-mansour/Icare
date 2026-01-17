package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun BottomBarNavigation(
    backStack: SnapshotStateList<Any>,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier,
    onNavigate: (Route) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = barBackgroundColor,
        contentColor = contentColor,
    ) {
        val currentDestination = backStack.lastOrNull()
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
                selected = currentDestination == item.route,
                onClick = { onNavigate(item.route) },
            )
        }
    }
}

@PreviewLightDark
@Composable
fun BottomNavItemPreview() {
    val bottomNavItems =
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Notifications,
            BottomNavItem.Profile,
            BottomNavItem.Settings,
        )
    BottomBarNavigation(
        backStack = remember { mutableStateListOf(Route.Splash) },
        items = bottomNavItems,
    ) {}
}
