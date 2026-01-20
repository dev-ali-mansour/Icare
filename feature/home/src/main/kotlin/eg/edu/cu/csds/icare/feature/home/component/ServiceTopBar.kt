package eg.edu.cu.csds.icare.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ServiceTopBar(
    title: String,
    onNavigationIconClicked: () -> Unit,
    onSearchClicked: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = helveticaFamily,
            )
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        navigationIcon = {
            IconButton(onClick = {
                onNavigationIconClicked()
            }) {
                Icon(
                    painter = painterResource(R.drawable.core_ui_ic_arrow_back),
                    contentDescription = stringResource(R.string.core_ui_back),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    painter = painterResource(drawable.core_ui_ic_search),
                    contentDescription = stringResource(string.core_ui_icon),
                )
            }
        },
    )
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun ServiceTopBarPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            ServiceTopBar(
                title = stringResource(id = string.core_ui_app_name),
                onNavigationIconClicked = {},
                onSearchClicked = {},
            )
        }
    }
}
