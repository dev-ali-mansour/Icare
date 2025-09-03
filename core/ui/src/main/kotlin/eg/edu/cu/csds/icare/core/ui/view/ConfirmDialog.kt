package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.tintColor

@Composable
fun ConfirmDialog(
    backgroundColor: Color,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmed: () -> Unit,
    onCancelled: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        containerColor = backgroundColor,
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = title,
                color = tintColor.copy(alpha = 0.6f),
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontFamily = helveticaFamily,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = message,
                color = tintColor.copy(alpha = 0.6f),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = helveticaFamily,
                overflow = TextOverflow.Ellipsis,
            )
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = tintColor),
                onClick = { onConfirmed() },
            ) {
                Text(
                    text = stringResource(id = android.R.string.ok),
                    color = Color.White,
                    fontFamily = helveticaFamily,
                )
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = tintColor),
                onClick = { onCancelled() },
            ) {
                Text(
                    text = stringResource(id = android.R.string.cancel),
                    color = Color.White,
                    fontFamily = helveticaFamily,
                )
            }
        },
    )
}
