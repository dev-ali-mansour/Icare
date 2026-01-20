package eg.edu.cu.csds.icare.feature.notification

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun NotificationView(
    text: String,
    date: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .padding(XS_PADDING)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = MAX_SURFACE_WIDTH),
        color = contentBackgroundColor,
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
                color = contentColor,
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
                text = date,
                color = contentColor,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = helveticaFamily,
                maxLines = 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NotificationViewPreview() {
    Column(modifier = Modifier.background(color = backgroundColor)) {
        NotificationView(
            text = stringResource(R.string.feature_notifications_welcome_message),
            date = "12-12-2022 12:00 PM",
        )
    }
}
