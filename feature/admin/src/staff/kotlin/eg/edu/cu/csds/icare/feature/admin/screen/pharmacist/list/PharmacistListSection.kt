package eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.PharmacistView
import org.koin.androidx.compose.koinViewModel

@Composable
fun PharmacistListSection(
    modifier: Modifier = Modifier,
    navigateToPharmacistDetails: (Pharmacist) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onError: suspend (UiText) -> Unit,
) {
    val viewModel: PharmacistListViewModel = koinViewModel()
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(PharmacistListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is PharmacistListEffect.NavigateToPharmacistDetails -> {
                    navigateToPharmacistDetails(effect.pharmacist)
                }

                is PharmacistListEffect.UpdateFabExpanded -> {
                    onExpandStateChanged(effect.isExpanded)
                }

                is PharmacistListEffect.ShowError -> {
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
                        viewModel.handleIntent(PharmacistListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        PharmacistListContent(
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
private fun PharmacistListContent(
    modifier: Modifier,
    uiState: PharmacistListState,
    onIntent: (PharmacistListIntent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val gridState = rememberLazyGridState()
        val expandedFabState = remember { derivedStateOf { gridState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(PharmacistListIntent.UpdateFabExpanded(expandedFabState.value))
        }
        if (uiState.pharmacists.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(string.core_ui_no_pharmacists_data),
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
                    items = uiState.pharmacists,
                    key = { it.id },
                    span = { GridItemSpan(1) },
                ) { pharmacist ->
                    PharmacistView(
                        name = pharmacist.name,
                        pharmacyName = pharmacist.pharmacyName,
                        email = pharmacist.email,
                        phone = pharmacist.phone,
                        profilePicture = pharmacist.profilePicture,
                    ) {
                        onIntent(PharmacistListIntent.SelectPharmacist(pharmacist))
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
internal fun PharmacistListSectionPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(M_PADDING),
        ) {
            PharmacistListContent(
                modifier = Modifier,
                uiState = PharmacistListState(),
                onIntent = {},
            )
        }
    }
}
