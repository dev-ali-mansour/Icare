package eg.edu.cu.csds.icare.feature.home.screen.lab

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
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.util.AdaptiveGrid
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.CenterView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.feature.home.R
import eg.edu.cu.csds.icare.feature.home.common.TopBar
import eg.edu.cu.csds.icare.feature.home.component.ServiceTopBar
import eg.edu.cu.csds.icare.feature.home.component.ServiceTopSearchBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabCenterListScreen(onNavigationIconClicked: () -> Unit) {
    val viewModel: LabListViewModel = koinViewModel()
    val adaptiveGrid: AdaptiveGrid = koinInject()
    val context: Context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(LabListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is LabListEffect.OnBackClick -> {
                    onNavigationIconClicked()
                }

                is LabListEffect.ShowError -> {
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
            when (uiState.topBar) {
                is TopBar.ServiceTopBar -> {
                    ServiceTopBar(
                        title = stringResource(string.core_ui_lab_centers),
                        onNavigationIconClicked = {
                            viewModel.handleIntent(LabListIntent.OnBackClick)
                        },
                        onSearchClicked = {
                            viewModel.handleIntent(
                                LabListIntent.ChangeTopBar(TopBar.ServiceSearchTopBar),
                            )
                            scope.launch {
                                gridState.scrollToItem(0)
                                delay(timeMillis = 100L)
                                keyboardController?.show()
                            }
                        },
                    )
                }

                is TopBar.ServiceSearchTopBar -> {
                    ServiceTopSearchBar(
                        placeholderText =
                            stringResource(R.string.feature_home_search_by_lab_name_or_address),
                        searchQuery = uiState.searchQuery,
                        onTextChanged = {
                            viewModel.handleIntent(LabListIntent.UpdateSearchQuery(it))
                        },
                        onSearch = {
                            keyboardController?.hide()
                        },
                        onCloseClicked = {
                            viewModel.handleIntent(
                                LabListIntent.ChangeTopBar(TopBar.ServiceTopBar),
                            )
                            scope.launch {
                                gridState.scrollToItem(0)
                            }
                        },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.handleIntent(LabListIntent.Refresh)
                        },
                    ).padding(paddingValues),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxSize(),
            ) {
                val (content, refresh) = createRefs()

                LabCenterListContent(
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
                    columnsCont = adaptiveGrid.calculateGridColumns(),
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
private fun LabCenterListContent(
    uiState: LabListState,
    modifier: Modifier = Modifier,
    columnsCont: Int = 1,
    gridState: LazyGridState = rememberLazyGridState(),
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        if (uiState.labs.isEmpty()) {
            EmptyContentView(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(R.string.feature_home_no_data_matched),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnsCont),
                modifier = modifier.fillMaxSize(),
                state = gridState,
                contentPadding = PaddingValues(all = S_PADDING),
                horizontalArrangement = Arrangement.spacedBy(S_PADDING),
                verticalArrangement = Arrangement.spacedBy(S_PADDING),
            ) {
                items(
                    uiState.labs,
                    key = { lab ->
                        lab.id
                    },
                    span = { GridItemSpan(1) },
                ) { lab ->
                    CenterView(
                        type = lab.type,
                        name = lab.name,
                        phone = lab.phone,
                        address = lab.address,
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
private fun LabListContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        LabCenterListContent(
            uiState =
                LabListState(
                    labs =
                        listOf(
                            LabImagingCenter(
                                id = 1,
                                name = "Alfa",
                                type = 1,
                                phone = "123456789",
                                address = "Address 1",
                            ),
                            LabImagingCenter(
                                id = 2,
                                name = "Beta",
                                type = 1,
                                phone = "123498789",
                                address = "Address 1",
                            ),
                            LabImagingCenter(
                                id = 3,
                                name = "El-Borg",
                                type = 1,
                                phone = "123456789",
                                address = "Address 1",
                            ),
                            LabImagingCenter(
                                id = 4,
                                name = "El-Shams",
                                type = 1,
                                phone = "123456789",
                                address = "Address 1",
                            ),
                        ),
                ),
            columnsCont = AdaptiveGrid().calculateGridColumns(),
        )
    }
}
