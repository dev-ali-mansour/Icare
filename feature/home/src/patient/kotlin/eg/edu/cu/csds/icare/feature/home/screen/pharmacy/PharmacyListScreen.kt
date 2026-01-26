package eg.edu.cu.csds.icare.feature.home.screen.pharmacy

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.CustomTopSearchBar
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.PharmacyView
import eg.edu.cu.csds.icare.core.ui.view.TopSearchBarState
import eg.edu.cu.csds.icare.feature.home.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyListScreen(onNavigationIconClicked: () -> Unit) {
    val viewModel: PharmacyListViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(PharmacyListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is PharmacyListEffect.OnBackClick -> {
                    onNavigationIconClicked()
                }

                is PharmacyListEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        },
    )

    Scaffold(
        topBar = {
            CustomTopSearchBar(
                status = uiState.searchBarState,
                title = stringResource(string.core_ui_pharmacies),
                placeholderText = stringResource(R.string.feature_home_search_by_pharmacy_name_or_address),
                searchQuery = uiState.searchQuery,
                onNavigationIconClicked = {
                    viewModel.handleIntent(PharmacyListIntent.OnBackClick)
                },
                onSearchClicked = {
                    viewModel.handleIntent(
                        PharmacyListIntent.UpdateSearchTopBarState(TopSearchBarState.Expanded),
                    )
                    scope.launch {
                        gridState.animateScrollToItem(0)
                        delay(timeMillis = 100L)
                        keyboardController?.show()
                    }
                },
                onTextChanged = {
                    viewModel.handleIntent(PharmacyListIntent.UpdateSearchQuery(it))
                },
                onSearch = {
                    keyboardController?.hide()
                },
                onCloseClicked = {
                    viewModel.handleIntent(
                        PharmacyListIntent.UpdateSearchTopBarState(TopSearchBarState.Collapsed),
                    )
                    scope.launch {
                        gridState.animateScrollToItem(0)
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.handleIntent(PharmacyListIntent.Refresh)
                        },
                    ).padding(innerPadding),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxSize(),
            ) {
                val (content, refresh) = createRefs()

                PharmacyListContent(
                    uiState = uiState,
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    gridState = gridState,
                )

                Indicator(
                    modifier =
                        Modifier.constrainAs(refresh) {
                            top.linkTo(parent.top, margin = XL_PADDING)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    isRefreshing = uiState.isLoading,
                    state = refreshState,
                )
            }
        }
    }
}

@Composable
private fun PharmacyListContent(
    uiState: PharmacyListState,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
) {
    Surface(modifier = modifier.fillMaxSize()) {
        if (uiState.pharmacies.isEmpty()) {
            EmptyContentView(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(R.string.feature_home_no_data_matched),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(calculateGridColumns()),
                modifier = modifier.fillMaxSize(),
                state = gridState,
                contentPadding = PaddingValues(all = S_PADDING),
                horizontalArrangement = Arrangement.spacedBy(S_PADDING),
                verticalArrangement = Arrangement.spacedBy(S_PADDING),
            ) {
                items(
                    uiState.pharmacies,
                    key = { pharmacy ->
                        pharmacy.id
                    },
                    span = { GridItemSpan(1) },
                ) { pharmacy ->
                    PharmacyView(
                        name = pharmacy.name,
                        phone = pharmacy.phone,
                        address = pharmacy.address,
                        modifier = modifier,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun PharmacyListContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            PharmacyListContent(
                uiState =
                    PharmacyListState(
                        pharmacies =
                            listOf(
                                Pharmacy(
                                    id = 1,
                                    name = "Pharmacy 1",
                                    address = "Address 1",
                                    phone = "123456789",
                                ),
                                Pharmacy(
                                    id = 2,
                                    name = "Pharmacy 2",
                                    address = "Address 2",
                                    phone = "987654321",
                                ),
                                Pharmacy(
                                    id = 3,
                                    name = "Pharmacy 3",
                                    address = "Address 3",
                                    phone = "555555555",
                                ),
                                Pharmacy(
                                    id = 4,
                                    name = "Pharmacy 4",
                                    address = "Address 4",
                                    phone = "111111111",
                                ),
                                Pharmacy(
                                    id = 5,
                                    name = "Pharmacy 5",
                                    address = "Address 5",
                                    phone = "111111111",
                                ),
                            ),
                    ),
            )
        }
    }
}
