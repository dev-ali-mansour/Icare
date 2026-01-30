package eg.edu.cu.csds.icare.feature.notification.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.constraintlayout.compose.ConstraintLayout
import eg.edu.cu.csds.icare.core.data.util.getFormattedDateTime
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TabRowContainerColor
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.feature.notification.R

@Composable
fun NotificationView(
    text: String,
    date: Long,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = MAX_SURFACE_WIDTH),
        color = TabRowContainerColor,
        shape =
            RoundedCornerShape(
                topStart = XS_PADDING,
                topEnd = XS_PADDING,
                bottomStart = XS_PADDING,
                bottomEnd = XS_PADDING,
            ),
    ) {
        ConstraintLayout(modifier = Modifier.padding(S_PADDING)) {
            val (message, dateTime) = createRefs()

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(message) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = helveticaFamily,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(dateTime) {
                            top.linkTo(message.bottom)
                            end.linkTo(message.end)
                        },
                textAlign = TextAlign.End,
                text = date.getFormattedDateTime(LocalContext.current),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = helveticaFamily,
                maxLines = 1,
            )
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@Composable
fun NotificationViewPreview() {
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        NotificationView(
            text = stringResource(R.string.feature_notifications_welcome_message),
            date = System.currentTimeMillis(),
        )
    }
}
