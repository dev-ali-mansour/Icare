package eg.edu.cu.csds.icare.feature.notification.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.core.domain.model.Notification
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.feature.notification.NotificationView
import eg.edu.cu.csds.icare.feature.notification.R
import java.util.Calendar

@Composable
internal fun NotificationsContent(modifier: Modifier = Modifier) {
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .padding(M_PADDING),
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
                        date = notification.date,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun NotificationsContentPreview() {
    Box(modifier = Modifier.background(color = backgroundColor)) {
        NotificationsContent()
    }
}
