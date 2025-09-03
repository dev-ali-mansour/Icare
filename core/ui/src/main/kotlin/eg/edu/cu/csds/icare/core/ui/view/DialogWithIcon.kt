package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.SMALL_SPINNER_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun DialogWithIcon(
    painter: Painter = painterResource(id = R.drawable.core_ui_ic_alert),
    text: String,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(SMALL_SPINNER_WIDTH),
            shape = RoundedCornerShape(M_PADDING),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(M_PADDING),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Yellow700,
                )
                Text(
                    text = text,
                    color = dropDownTextColor,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontFamily = helveticaFamily,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DialogWithIconPreview() {
    Box(
        modifier =
            Modifier
                .padding(XL_PADDING)
                .background(color = backgroundColor),
    ) {
        DialogWithIcon(
            painter = painterResource(id = R.drawable.core_ui_ic_alert),
            text = "فشل في الاتصال بالسيرفر",
        ) {
        }
    }
}
