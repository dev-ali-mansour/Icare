package eg.edu.cu.csds.icare.feature.notification.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import eg.edu.cu.csds.icare.core.domain.model.Notification
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.currentLanguage
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.feature.notification.NotificationView
import eg.edu.cu.csds.icare.feature.notification.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
internal fun NotificationsScreen() {
    Column(
        modifier =
            Modifier
                .background(backgroundColor)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        NotificationsContent()
    }
}

@Composable
private fun NotificationsContent(modifier: Modifier = Modifier) {
    Surface(
        modifier =
            modifier
                .padding(M_PADDING)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = 600.dp),
        color = backgroundColor,
        tonalElevation = S_PADDING,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val notifications =
                listOf(
                    Notification(
                        id = 1,
                        text = stringResource(R.string.feature_notifications_welcome_message),
                        date = Calendar.getInstance().time,
                    ),
                )

            Text(
                modifier = Modifier.padding(bottom = S_PADDING),
                text = stringResource(eg.edu.cu.csds.icare.core.ui.R.string.core_ui_nav_notifications),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = helveticaFamily,
                color = textColor,
                maxLines = 1,
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(XS_PADDING),
            ) {
                items(notifications) { notification ->
                    NotificationView(
                        text = notification.text,
                        date =
                            SimpleDateFormat(
                                "dd-MM-yyyy HH:mm aa",
                                Locale.forLanguageTag(currentLanguage.code),
                            ).format(
                                notification.date,
                            ),
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun NotificationsContentPreview() {
    Box(modifier = Modifier.background(color = backgroundColor)) {
        NotificationsContent()
    }
}
