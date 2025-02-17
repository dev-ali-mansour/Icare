package eg.edu.cu.csds.icare.core.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    showLeadingIcon: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Number,
    placeholder: String,
    value: String,
    focus: Boolean,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        shape = RoundedCornerShape(S_PADDING),
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                modifier = Modifier.alpha(alpha = 0.6f),
                text = placeholder,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = helveticaFamily,
                color = textColor,
                maxLines = 1,
            )
        },
        textStyle =
            TextStyle.Default.copy(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = helveticaFamily,
                color = textColor,
            ),
        singleLine = true,
        leadingIcon = {
            if (showLeadingIcon) {
                IconButton(
                    modifier = Modifier.alpha(alpha = 0.6f),
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Yellow700,
                    )
                }
            }
        },
        trailingIcon = {
            if (value.trim().isNotEmpty()) {
                IconButton(onClick = { onClear() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Yellow700,
                    )
                }
            }
        },
        keyboardOptions =
            KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Search,
            ),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
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
    )

    LaunchedEffect(Unit) {
        if (focus) focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchTextFieldPreview() {
    Box(modifier = Modifier.background(color = backgroundColor)) {
        SearchTextField(
            placeholder = stringResource(R.string.search_here),
            value = "",
            focus = true,
            onValueChange = {},
            onClear = { },
        ) {
        }
    }
}
