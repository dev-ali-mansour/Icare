package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.list

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
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.PharmacyView
import org.koin.androidx.compose.koinViewModel

@Composable
fun PharmacyListSection(
    modifier: Modifier = Modifier,
    navigateToPharmacyDetails: (Pharmacy) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onError: suspend (UiText) -> Unit,
) {
    val viewModel: PharmacyListViewModel = koinViewModel()
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(PharmacyListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is PharmacyListEffect.NavigateToPharmacyDetails -> {
                    navigateToPharmacyDetails(effect.pharmacy)
                }

                is PharmacyListEffect.UpdateFabExpanded -> {
                    onExpandStateChanged(effect.isExpanded)
                }

                is PharmacyListEffect.ShowError -> {
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
                        viewModel.handleIntent(PharmacyListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        PharmacyListContent(
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
private fun PharmacyListContent(
    modifier: Modifier = Modifier,
    uiState: PharmacyListState,
    onIntent: (PharmacyListIntent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val gridState = rememberLazyGridState()
        val expandedFabState = remember { derivedStateOf { gridState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(PharmacyListIntent.UpdateFabExpanded(expandedFabState.value))
        }

        if (uiState.pharmacies.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(string.core_ui_no_pharmacies_data),
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
                    items = uiState.pharmacies,
                    key = { it.id },
                    span = { GridItemSpan(1) },
                ) { pharmacy ->
                    PharmacyView(
                        name = pharmacy.name,
                        phone = pharmacy.phone,
                        address = pharmacy.address,
                    ) {
                        onIntent(PharmacyListIntent.SelectPharmacy(pharmacy))
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
internal fun PharmacyContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            PharmacyListContent(
                uiState =
                    PharmacyListState(
                        pharmacies =
                            listOf(
                                Pharmacy(
                                    id = 1,
                                    name = "صيدلية الحياة",
                                    address = "38 ش فيصل - حسن  محمد",
                                    phone = "123-456-7890",
                                ),
                                Pharmacy(
                                    id = 2,
                                    name = "صيدلية يحيى",
                                    address = "45 ش أسامة أبو عميرة - متفرع من ش فيصل",
                                    phone = "987-654-3210",
                                ),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
