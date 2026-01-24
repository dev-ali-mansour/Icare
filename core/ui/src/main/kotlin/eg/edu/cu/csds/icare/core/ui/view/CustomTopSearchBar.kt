package eg.edu.cu.csds.icare.core.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopSearchBar(
    status: TopSearchBarState,
    title: String,
    modifier: Modifier = Modifier,
    placeholderText: String = stringResource(R.string.core_ui_search_here),
    searchQuery: String = "",
    onNavigationIconClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    when (status) {
        is TopSearchBarState.Collapsed -> {
            CollapsedSearchTopBar(
                title = title,
                onNavigationIconClicked =
                onNavigationIconClicked,
                onSearchClicked = onSearchClicked,
            )
        }

        is TopSearchBarState.Expanded -> {
            ExpandedTopSearchBar(
                modifier = modifier,
                placeholderText = placeholderText,
                searchQuery = searchQuery,
                onTextChanged = onTextChanged,
                onSearch = onSearch,
                onCloseClicked = onCloseClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsedSearchTopBar(
    title: String,
    onNavigationIconClicked: () -> Unit,
    onSearchClicked: () -> Unit,
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    fontFamily = helveticaFamily,
                )
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            navigationIcon = {
                IconButton(onClick = {
                    onNavigationIconClicked()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.core_ui_ic_arrow_back),
                        contentDescription = stringResource(R.string.core_ui_back),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            actions = {
                IconButton(onClick = onSearchClicked) {
                    Icon(
                        painter = painterResource(R.drawable.core_ui_ic_search),
                        contentDescription = stringResource(R.string.core_ui_icon),
                    )
                }
            },
        )

        Box(
            modifier =
                Modifier
                    .background(Yellow500)
                    .fillMaxWidth()
                    .height(XS_PADDING),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandedTopSearchBar(
    modifier: Modifier = Modifier,
    placeholderText: String = stringResource(R.string.core_ui_search_here),
    searchQuery: String = "",
    onTextChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val textFieldState = rememberTextFieldState(searchQuery)
    val searchBarState = rememberSearchBarState()
    val cancelString = stringResource(android.R.string.cancel)

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
        Column {
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
                                    painter = painterResource(R.drawable.core_ui_ic_search),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                modifier =
                                    Modifier.semantics {
                                        contentDescription = cancelString
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
                                    painter = painterResource(R.drawable.core_ui_ic_close),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                    )
                },
            )

            Box(
                modifier =
                    Modifier
                        .background(Yellow500)
                        .fillMaxWidth()
                        .height(XS_PADDING),
            )
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun CustomTopSearchBarPreview() {
    IcareTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            CustomTopSearchBar(
                status = TopSearchBarState.Collapsed,
                title = stringResource(R.string.core_ui_app_name),
                onNavigationIconClicked = {},
                onSearchClicked = {},
                onTextChanged = {},
                onSearch = {},
                onCloseClicked = {},
            )

            Spacer(modifier = Modifier.height(S_PADDING))

            CustomTopSearchBar(
                status = TopSearchBarState.Expanded,
                title = stringResource(R.string.core_ui_app_name),
                onNavigationIconClicked = {},
                onSearchClicked = {},
                onTextChanged = {},
                onSearch = {},
                onCloseClicked = {},
            )
        }
    }
}

@Stable
sealed interface TopSearchBarState {
    data object Collapsed : TopSearchBarState

    data object Expanded : TopSearchBarState
}
