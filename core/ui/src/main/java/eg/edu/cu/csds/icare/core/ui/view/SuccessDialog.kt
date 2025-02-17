package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import eg.edu.cu.csds.icare.core.ui.theme.Blue500
import eg.edu.cu.csds.icare.core.ui.theme.DIALOG_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun SuccessesDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(DIALOG_CARD_HEIGHT)
                    .padding(M_PADDING),
            shape = RoundedCornerShape(M_PADDING),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                GifImageLoader(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.7f),
                )
                Text(
                    text = "عملية ناجحة",
                    modifier = Modifier,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontFamily = helveticaFamily,
                    textAlign = TextAlign.Center,
                    color = Blue500,
                )
            }
        }
    }
}
