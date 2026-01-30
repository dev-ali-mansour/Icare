package eg.edu.cu.csds.icare.feature.admin.screen.center.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.CenterView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import org.koin.androidx.compose.koinViewModel

@Composable
fun CenterListSection(
    modifier: Modifier = Modifier,
    navigateToCenterDetails: (LabImagingCenter) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onError: suspend (UiText) -> Unit,
) {
    val viewModel: CenterListViewModel = koinViewModel()
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(CenterListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is CenterListEffect.NavigateToCenterDetails -> {
                    navigateToCenterDetails(effect.center)
                }

                is CenterListEffect.UpdateFabExpanded -> {
                    onExpandStateChanged(effect.isExpanded)
                }

                is CenterListEffect.ShowError -> {
                    onError(effect.message)
                }
            }
        },
    )

    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .pullToRefresh(
                    state = refreshState,
                    isRefreshing = uiState.isLoading,
                    onRefresh = {
                        viewModel.handleIntent(CenterListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        CenterListContent(
            modifier =
                Modifier.constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            uiState = uiState,
            onIntent = viewModel::handleIntent,
        )

        Indicator(
            modifier =
                Modifier
                    .constrainAs(refresh) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            isRefreshing = uiState.isLoading,
            state = refreshState,
        )
    }
}

@Composable
fun CenterListContent(
    modifier: Modifier = Modifier,
    uiState: CenterListState,
    onIntent: (CenterListIntent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val gridState = rememberLazyGridState()
        val expandedFabState = remember { derivedStateOf { gridState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(CenterListIntent.UpdateFabExpanded(expandedFabState.value))
        }

        if (uiState.centers.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(string.core_ui_no_centers_data),
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
                    items = uiState.centers,
                    key = { it.id },
                    span = { GridItemSpan(1) },
                ) { center ->
                    CenterView(
                        name = center.name,
                        type = center.type,
                        phone = center.phone,
                        address = center.address,
                        showType = true,
                    ) {
                        onIntent(CenterListIntent.SelectCenter(center))
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
internal fun ClinicsContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            CenterListContent(
                uiState =
                    CenterListState(
                        centers =
                            listOf(
                                LabImagingCenter(
                                    id = 1,
                                    type = 1,
                                    name = "Alfa",
                                    phone = "0123456789",
                                    address = "53 Faysal street,Giza,Egypt",
                                ),
                                LabImagingCenter(
                                    id = 2,
                                    type = 2,
                                    name = "Beta",
                                    phone = "0123456789",
                                    address = "13 Faysal street,Giza,Egypt",
                                ),
                                LabImagingCenter(
                                    id = 3,
                                    type = 1,
                                    name = "Alfa",
                                    phone = "0123456789",
                                    address = "53 Faysal street,Giza,Egypt",
                                ),
                                LabImagingCenter(
                                    id = 4,
                                    type = 2,
                                    name = "Beta",
                                    phone = "0123456789",
                                    address = "13 Faysal street,Giza,Egypt",
                                ),
                                LabImagingCenter(
                                    id = 5,
                                    type = 1,
                                    name = "Alfa",
                                    phone = "0123456789",
                                    address = "53 Faysal street,Giza,Egypt",
                                ),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
