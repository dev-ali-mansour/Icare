package eg.edu.cu.csds.icare.feature.admin.screen.doctor.list

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
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.DoctorView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import org.koin.androidx.compose.koinViewModel

@Composable
fun DoctorListSection(
    modifier: Modifier = Modifier,
    navigateToDoctorDetails: (Doctor) -> Unit,
    onExpandStateChanged: (Boolean) -> Unit,
    onError: suspend (UiText) -> Unit,
) {
    val viewModel: DoctorListViewModel = koinViewModel()
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(DoctorListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is DoctorListEffect.NavigateToDoctorDetails -> {
                    navigateToDoctorDetails(effect.doctor)
                }

                is DoctorListEffect.UpdateFabExpanded -> {
                    onExpandStateChanged(effect.isExpanded)
                }

                is DoctorListEffect.ShowError -> {
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
                        viewModel.handleIntent(DoctorListIntent.Refresh)
                    },
                ),
    ) {
        val (refresh, content) = createRefs()

        DoctorListContent(
            modifier =
                Modifier.constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            state = uiState,
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
private fun DoctorListContent(
    modifier: Modifier,
    state: DoctorListState,
    onIntent: (DoctorListIntent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val gridState = rememberLazyGridState()
        val expandedFabState = remember { derivedStateOf { gridState.firstVisibleItemIndex == 0 } }
        LaunchedEffect(key1 = expandedFabState.value) {
            onIntent(DoctorListIntent.UpdateFabExpanded(expandedFabState.value))
        }

        if (state.doctors.isEmpty()) {
            EmptyContentView(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(eg.edu.cu.csds.icare.core.ui.R.string.core_ui_no_doctors_data),
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
                    items = state.doctors,
                    key = { it.id },
                    span = { GridItemSpan(1) },
                ) { doctor ->
                    DoctorView(
                        name = doctor.name,
                        specialty = doctor.specialty,
                        availability = doctor.availability,
                        profilePicture = doctor.profilePicture,
                    ) {
                        onIntent(DoctorListIntent.SelectDoctor(doctor))
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
internal fun DoctorListContentPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(M_PADDING),
        ) {
            DoctorListContent(
                modifier = Modifier,
                state =
                    DoctorListState(
                        doctors =
                            listOf(
                                Doctor(
                                    firstName = "John",
                                    lastName = "Doe",
                                    specialty = "Cardiology",
                                ),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
