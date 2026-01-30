package eg.edu.cu.csds.icare.feature.notification.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.domain.model.Notification
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.feature.notification.R
import eg.edu.cu.csds.icare.feature.notification.view.NotificationView
import java.util.Calendar

@Composable
internal fun NotificationsScreen() {
    val gridState = rememberLazyGridState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NotificationsContent(
                notifications =
                    listOf(
                        Notification(
                            id = 1,
                            text = stringResource(R.string.feature_notifications_welcome_message),
                            date = Calendar.getInstance().time.time,
                        ),
                    ),
                gridState = gridState,
            )
        }
    }
}

@Composable
private fun NotificationsContent(
    notifications: List<Notification>,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(M_PADDING),
    ) {
        Text(
            modifier = Modifier.padding(bottom = S_PADDING),
            text = stringResource(string.core_ui_nav_notifications),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontFamily = helveticaFamily,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(calculateGridColumns()),
            modifier = modifier.fillMaxSize(),
            state = gridState,
            contentPadding = PaddingValues(vertical = S_PADDING),
            horizontalArrangement = Arrangement.spacedBy(S_PADDING),
            verticalArrangement = Arrangement.spacedBy(S_PADDING),
        ) {
            items(
                items = notifications,
                key = { notification ->
                    notification.id
                },
                span = { GridItemSpan(1) },
            ) { notification ->
                NotificationView(
                    text = notification.text,
                    date = notification.date,
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun NotificationsContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            NotificationsContent(
                notifications =
                    listOf(
                        Notification(
                            id = 1,
                            text = stringResource(R.string.feature_notifications_welcome_message),
                            date = Calendar.getInstance().time.time,
                        ),
                        Notification(
                            id = 2,
                            text = "Notification 2",
                            date = Calendar.getInstance().time.time,
                        ),
                        Notification(
                            id = 3,
                            text = "Notification 3",
                            date = Calendar.getInstance().time.time,
                        ),
                        Notification(
                            id = 4,
                            text = "Notification 4",
                            date = Calendar.getInstance().time.time,
                        ),
                        Notification(
                            id = 5,
                            text = "Notification 5",
                            date = Calendar.getInstance().time.time,
                        ),
                    ),
            )
        }
    }
}
