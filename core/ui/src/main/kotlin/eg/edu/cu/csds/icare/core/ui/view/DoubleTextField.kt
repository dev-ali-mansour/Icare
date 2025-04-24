package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var text by remember { mutableStateOf(value.formatForDisplay()) }
    var lastValidValue by remember(value) { mutableDoubleStateOf(value) }

    // Sync with external value changes
    LaunchedEffect(value) {
        if (value != lastValidValue) {
            text = value.formatForDisplay()
            lastValidValue = value
        }
    }

    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            when {
                newText.isEmpty() -> {
                    onValueChanged(0.0)
                    lastValidValue = 0.0
                }
                newText.isValidDoubleInput() -> {
                    val doubleValue = newText.toDoubleOrNull() ?: 0.0
                    onValueChanged(doubleValue)
                    lastValidValue = doubleValue
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
