package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.tintColor

@Composable
fun ConfirmDialog(
    modifier: Modifier = Modifier,
    openDialog: MutableState<Boolean>,
    backgroundColor: Color,
    title: String,
    message: String,
    cancellable: Boolean,
    onConfirmed: () -> Unit,
    onCancelled: () -> Unit,
) {
    if (openDialog.value) {
        AlertDialog(
            modifier = modifier,
            containerColor = backgroundColor,
            onDismissRequest = {
                if (cancellable) openDialog.value = false
            },
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
                    onClick = {
                        openDialog.value = false
                        onConfirmed()
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = Color.White,
                        fontFamily = helveticaFamily,
                    )
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = tintColor),
                    onClick = {
                        openDialog.value = false
                        onCancelled()
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = Color.White,
                        fontFamily = helveticaFamily,
                    )
                }
            },
        )
    }
}
