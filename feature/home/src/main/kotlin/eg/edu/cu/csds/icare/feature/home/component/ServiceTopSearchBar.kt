package eg.edu.cu.csds.icare.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.tintColor
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.UiText.DynamicString
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ServiceTopSearchBar(
    modifier: Modifier = Modifier,
    placeholderText: String = stringResource(R.string.core_ui_search_here),
    searchQuery: String = "",
    onTextChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val textFieldState = rememberTextFieldState(searchQuery)
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .distinctUntilChanged()
            .collectLatest { newValue ->
                if (newValue != searchQuery) {
                    onTextChanged(newValue)
                }
            }
    }

    Surface(color = MaterialTheme.colorScheme.primary) {
        TopSearchBar(
            state = searchBarState,
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = modifier,
                    searchBarState = searchBarState,
                    textFieldState = textFieldState,
                    onSearch = { onSearch(it) },
                    colors =
                        SearchBarDefaults.inputFieldColors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent.copy(alpha = 0.6f),
                            cursorColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(alpha = 0.6f),
                            text = placeholderText,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    leadingIcon = {
                        IconButton(
                            modifier = Modifier.alpha(alpha = 0.6f),
                            onClick = {},
                        ) {
                            Icon(
                                painter = painterResource(drawable.core_ui_ic_search),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            modifier =
                                Modifier.semantics {
                                    contentDescription = context.getString(android.R.string.cancel)
                                },
                            onClick = {
                                focusManager.clearFocus()
                                val currentText = textFieldState.text.toString()
                                if (currentText.isNotEmpty()) {
                                    textFieldState.edit { replace(0, currentText.length, "") }
                                    onTextChanged("")
                                } else {
                                    onCloseClicked()
                                }
                            },
                        ) {
                            Icon(
                                painter = painterResource(drawable.core_ui_ic_close),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                )
            },
        )
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun SearchWidgetPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            ServiceTopSearchBar(
                onTextChanged = {},
                onSearch = {},
                onCloseClicked = {},
            )
        }
    }
}
