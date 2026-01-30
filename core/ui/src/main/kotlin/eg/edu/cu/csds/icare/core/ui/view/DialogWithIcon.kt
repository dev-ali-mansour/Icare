package eg.edu.cu.csds.icare.core.ui.view

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.LANGUAGE_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.SMALL_SPINNER_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark

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

@PreviewLightDark
@PreviewArabicLightDark
@Composable
fun DialogWithIconPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(M_PADDING)
                    .fillMaxWidth()
                    .height(LANGUAGE_CARD_HEIGHT),
        ) {
            DialogWithIcon(
                painter = painterResource(id = R.drawable.core_ui_ic_alert),
                text = stringResource(id = R.string.core_ui_error_server),
            ) {
            }
        }
    }
}
