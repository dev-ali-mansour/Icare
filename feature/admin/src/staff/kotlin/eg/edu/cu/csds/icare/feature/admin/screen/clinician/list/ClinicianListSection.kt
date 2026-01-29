package eg.edu.cu.csds.icare.feature.admin.screen.clinician.list

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
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.ClinicianView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClinicianListSection(
    modifier: Modifier = Modifier,
    navigateToClinicianDetails: (Clinician) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onError: suspend (UiText) -> Unit,
) {
    val viewModel: ClinicianListViewModel = koinViewModel()
    val refreshState = rememberPullToRefreshState()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(ClinicianListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is ClinicianListEffect.NavigateToClinicianDetails -> {
                    navigateToClinicianDetails(effect.clinician)
                }

                is ClinicianListEffect.UpdateFabExpanded -> {
                    onExpandStateChanged(effect.isExpanded)
                }

                is ClinicianListEffect.ShowError -> {
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
                        viewModel.handleIntent(ClinicianListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        ClinicianListContent(
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
private fun ClinicianListContent(
    modifier: Modifier,
    state: ClinicianListState,
    onIntent: (ClinicianListIntent) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        val gridState = rememberLazyGridState()
        val expandedFabState = remember { derivedStateOf { gridState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(ClinicianListIntent.UpdateFabExpanded(expandedFabState.value))
        }

        if (state.clinicians.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier.fillMaxSize(),
                text = stringResource(eg.edu.cu.csds.icare.core.ui.R.string.core_ui_no_clinicians_data),
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
                    items = state.clinicians,
                    key = { it.id },
                    span = { GridItemSpan(1) },
                ) { clinician ->
                    ClinicianView(
                        name = clinician.name,
                        clinicName = clinician.clinicName,
                        email = clinician.email,
                        phone = clinician.phone,
                        profilePicture = clinician.profilePicture,
                    ) {
                        onIntent(ClinicianListIntent.SelectClinician(clinician))
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
internal fun ClinicStaffsContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            ClinicianListContent(
                modifier = Modifier,
                state = ClinicianListState(
                    clinicians = listOf(
                        Clinician(
                            id="1",
                            name = "Clinician 1",
                            clinicName = "Clinic Name",
                            email = "clinic@mail.com",
                            phone = "01210015678",
                        ),
                        Clinician(
                            id="2",
                            name = "Clinician 2",
                            clinicName = "Clinic Name",
                            email = "clinic@mail.com",
                            phone = "01210015678",
                        ),
                        Clinician(
                            id="3",
                            name = "Clinician 3",
                            clinicName = "Clinic Name",
                            email = "clinic@mail.com",
                            phone = "01210015678",
                        ),
                        Clinician(
                            id="4",
                            name = "Clinician 4",
                            clinicName = "Clinic Name",
                            email = "clinic@mail.com",
                            phone = "01210015678",
                        ),
                        Clinician(
                            id="5",
                            name = "Clinician 5",
                            clinicName = "Clinic Name",
                            email = "clinic@mail.com",
                            phone = "01210015678",
                        ),
                    )
                ),
                onIntent = {},
            )
        }
    }
}
