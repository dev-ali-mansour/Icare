package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import eg.edu.cu.csds.icare.core.ui.common.BOTTOM_NAV_ENTRIES
import eg.edu.cu.csds.icare.core.ui.common.BottomNavItem
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark

@Composable
fun BottomBarNavigation(
    items: Set<BottomNavItem>,
    selectedKey: NavKey?,
    modifier: Modifier = Modifier,
    onSelect: (NavKey) -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = barBackgroundColor,
        contentColor = contentColor,
    ) {
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
                selected = selectedKey == item.key,
                onClick = { onSelect(item.key) },
            )
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@Composable
fun BottomNavItemPreview() {
    BottomBarNavigation(
        items = BOTTOM_NAV_ENTRIES,
        selectedKey = Route.Home,
    ) {}
}
