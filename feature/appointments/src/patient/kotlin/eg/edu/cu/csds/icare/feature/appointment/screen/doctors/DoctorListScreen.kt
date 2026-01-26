package eg.edu.cu.csds.icare.feature.appointment.screen.doctors

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.CustomTopSearchBar
import eg.edu.cu.csds.icare.core.ui.view.DoctorView
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.TopSearchBarState
import eg.edu.cu.csds.icare.feature.appointment.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DoctorListScreen(
    onNavigationIconClicked: () -> Unit,
    navigateToBookingRoute: (Doctor) -> Unit,
) {
    val viewModel: DoctorListViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(DoctorListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is DoctorListEffect.OnBackClick -> {
                    onNavigationIconClicked()
                }

                is DoctorListEffect.NavigateToBookingRoute -> {
                    navigateToBookingRoute(effect.doctor)
                }

                is DoctorListEffect.ShowError -> {
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
                title = stringResource(R.string.feature_appointments_doctor_list),
                placeholderText = stringResource(string.core_ui_search_by_doctor_name_or_speciality),
                searchQuery = uiState.searchQuery,
                onNavigationIconClicked = {
                    viewModel.handleIntent(DoctorListIntent.OnBackClick)
                },
                onSearchClicked = {
                    viewModel.handleIntent(
                        DoctorListIntent.UpdateSearchTopBarState(TopSearchBarState.Expanded),
                    )
                    scope.launch {
                        gridState.animateScrollToItem(0)
                        delay(timeMillis = 100L)
                        keyboardController?.show()
                    }
                },
                onTextChanged = {
                    viewModel.handleIntent(DoctorListIntent.UpdateSearchQuery(it))
                },
                onSearch = {
                    keyboardController?.hide()
                },
                onCloseClicked = {
                    viewModel.handleIntent(
                        DoctorListIntent.UpdateSearchTopBarState(TopSearchBarState.Collapsed),
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
                            viewModel.handleIntent(DoctorListIntent.Refresh)
                        },
                    ).padding(innerPadding),
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (content, refresh) = createRefs()

                DoctorsListContent(
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
private fun DoctorsListContent(
    uiState: DoctorListState,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    onIntent: (DoctorListIntent) -> Unit,
) {
    Surface(modifier = modifier.fillMaxSize()) {
        if (uiState.doctors.isEmpty()) {
            EmptyContentView(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                text = stringResource(R.string.feature_appointments_no_doctors_available),
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
                    items = uiState.doctors,
                    key = { doctor ->
                        doctor.id
                    },
                    span = { GridItemSpan(1) },
                ) { doctor ->
                    DoctorView(
                        name = doctor.name,
                        specialty = doctor.specialty,
                        availability = doctor.availability,
                        profilePicture = doctor.profilePicture,
                        onClick = { onIntent(DoctorListIntent.SelectDoctor(doctor)) },
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
private fun DoctorsListContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            DoctorsListContent(
                uiState =
                    DoctorListState(
                        doctors =
                            listOf(
                                Doctor(
                                    id = "1",
                                    firstName = "Dr. Anna ",
                                    lastName = "Jones",
                                    specialty = "General Practitioner",
                                ),
                                Doctor(
                                    id = "2",
                                    firstName = "Dr. Tiya ",
                                    lastName = "Mcdariel",
                                    specialty = "Heart Specialist",
                                ),
                                Doctor(
                                    id = "3",
                                    firstName = "Dr. John ",
                                    lastName = "Berry",
                                    specialty = "General Practitioner",
                                ),
                                Doctor(
                                    id = "4",
                                    firstName = "Dr. Sara ",
                                    lastName = "Mcdariel",
                                    specialty = "Heart Specialist",
                                ),
                                Doctor(
                                    id = "5",
                                    firstName = "Dr. Suzan ",
                                    lastName = "Berry",
                                    specialty = "General Practitioner",
                                ),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
