package eg.edu.cu.csds.icare.core.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    onNavigationIconClicked: () -> Unit,
) {
    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = title) },
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
            actions = actions,
        )

        Box(
            modifier =
                Modifier
                    .background(Yellow500)
                    .fillMaxWidth()
                    .height(XS_PADDING),
        )
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@Composable
private fun SplashScreenPreview() {
    IcareTheme {
        CommonTopAppBar(title = stringResource(R.string.core_ui_app_name)) {
        }
    }
}
