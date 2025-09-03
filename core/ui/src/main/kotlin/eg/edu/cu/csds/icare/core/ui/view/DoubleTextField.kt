package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import eg.edu.cu.csds.icare.core.domain.util.formatForDisplay
import eg.edu.cu.csds.icare.core.domain.util.isValidDoubleInput
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun DoubleTextField(
    value: Double,
    onValueChanged: (Double) -> Unit,
    label: String,
    textColor: Color,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(fraction = 0.8f),
    imeAction: ImeAction = ImeAction.Next,
) {
    val text = value.formatForDisplay()

    TextField(
        value = text,
        onValueChange = { newText ->
            when {
                newText.isEmpty() -> {
                    onValueChanged(0.0)
                }

                newText.isValidDoubleInput() -> {
                    val doubleValue = newText.toDoubleOrNull() ?: 0.0
                    onValueChanged(doubleValue)
                }
            }
        },
        label = {
            Text(
                text = label,
                fontFamily = helveticaFamily,
                color = textColor,
            )
        },
        readOnly = readOnly,
        singleLine = true,
        modifier = modifier,
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = contentColor,
                focusedTextColor = textColor,
                focusedIndicatorColor = Yellow500,
                unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
            ),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction,
            ),
        visualTransformation = DecimalTransformation(),
    )
}

private class DecimalTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text
        val formatted =
            when {
                input.isEmpty() -> ""
                input.endsWith(".") -> input
                else -> {
                    val parts = input.split(".")
                    when (parts.size) {
                        1 -> parts[0]
                        2 -> "${parts[0]}.${parts[1].take(2)}"
                        else -> input
                    }
                }
            }

        return TransformedText(
            AnnotatedString(formatted),
            OffsetMapping.Identity,
        )
    }
}
