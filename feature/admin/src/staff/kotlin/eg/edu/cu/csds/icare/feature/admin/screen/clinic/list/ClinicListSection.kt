package eg.edu.cu.csds.icare.feature.admin.screen.clinic.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.ClinicView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClinicListSection(
    modifier: Modifier = Modifier,
    navigateToClinicDetails: (Clinic) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onError: suspend (UiText) -> Unit,
) {
    val viewModel: ClinicListViewModel = koinViewModel()
    val refreshState = rememberPullToRefreshState()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(ClinicListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is ClinicListEffect.NavigateToClinicDetails -> {
                    navigateToClinicDetails(effect.clinic)
                }

                is ClinicListEffect.UpdateFabExpanded -> {
                    onExpandStateChanged(effect.isExpanded)
                }

                is ClinicListEffect.ShowError -> {
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
                    isRefreshing = state.isLoading,
                    onRefresh = {
                        viewModel.handleIntent(ClinicListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        ClinicListContent(
            modifier =
                Modifier.constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            state = state,
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
            isRefreshing = state.isLoading,
            state = refreshState,
        )
    }
}

@Composable
private fun ClinicListContent(
    modifier: Modifier,
    state: ClinicListState,
    onIntent: (ClinicListIntent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val listState = rememberLazyListState()
        val expandedFabState = remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(ClinicListIntent.UpdateFabExpanded(expandedFabState.value))
        }

        if (state.clinics.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(eg.edu.cu.csds.icare.core.ui.R.string.core_ui_no_clinics_data),
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(S_PADDING),
            ) {
                items(
                    items = state.clinics,
                    key = { clinic ->
                        clinic.id
                    },
                ) { clinic ->
                    ClinicView(clinic = clinic) {
                        onIntent(ClinicListIntent.SelectClinic(clinic))
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
            ClinicListContent(
                modifier = Modifier,
                state =
                    ClinicListState(
                        clinics =
                            listOf(
                                Clinic(
                                    id = 1,
                                    name = "عيادة 1",
                                    type = "مخ وأعصاب",
                                    address = "مبنى العيادات الخارجية - الدور الأول",
                                    phone = "0123456789",
                                    isOpen = true,
                                ),
                                Clinic(
                                    id = 2,
                                    name = "عيادة 2",
                                    type = "باطنة",
                                    address = "مبنى العيادات الخارجية - الدور الثالث",
                                    phone = "987654321",
                                    isOpen = false,
                                ),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
