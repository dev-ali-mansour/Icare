package eg.edu.cu.csds.icare.feature.appointment.screen.appointments

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.data.util.getFormattedDateTime
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.common.CommonTopAppBar
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import eg.edu.cu.csds.icare.feature.appointment.R
import eg.edu.cu.csds.icare.feature.appointment.component.AppointmentCard
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyAppointmentsScreen(
    onNavigationIconClicked: () -> Unit,
    navigateToRescheduleRoute: (Appointment) -> Unit,
) {
    val viewModel: AppointmentListViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val gridState = rememberLazyGridState()
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(AppointmentListIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is AppointmentListEffect.OnBackClick -> {
                    onNavigationIconClicked()
                }

                is AppointmentListEffect.NavigateToRescheduleAppointment -> {
                    navigateToRescheduleRoute(
                        effect.appointment,
                    )
                }

                is AppointmentListEffect.ShowSuccess -> {
                    showSuccessDialog = true
                    delay(timeMillis = 3000)
                    showSuccessDialog = false
                }

                is AppointmentListEffect.ShowError -> {
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
            CommonTopAppBar(
                title = stringResource(id = R.string.feature_appointments_my_appointments),
            ) {
                onNavigationIconClicked()
            }
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
                            viewModel.handleIntent(AppointmentListIntent.Refresh)
                        },
                    ).padding(innerPadding),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                val (content, refresh) = createRefs()

                MyAppointmentsContent(
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
                    gridState = gridState,
                    onIntent = viewModel::handleIntent,
                )

                Indicator(
                    modifier =
                        Modifier.constrainAs(refresh) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    isRefreshing = uiState.isLoading,
                    state = refreshState,
                )

                if (showSuccessDialog) SuccessesDialog {}
            }
        }
    }
}

@Composable
fun MyAppointmentsContent(
    uiState: AppointmentListState,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    onIntent: (AppointmentListIntent) -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val statusList =
        listOf(
            AppointmentStatus.PendingStatus,
            AppointmentStatus.ConfirmedStatus,
            AppointmentStatus.CompletedStatus,
            AppointmentStatus.CancelledStatus,
        )

    Surface(modifier = modifier.fillMaxSize()) {
        val status = statusList[selectedTabIndex]
        val appointments =
            uiState.appointments.filter { it.statusId == status.code }.map { appointment ->
                statusList.find { it.code == appointment.statusId }?.let {
                    appointment.copy(status = stringResource(it.textResId))
                } ?: appointment
            }
        val showActions = status.code == AppointmentStatus.PendingStatus.code
        Column(modifier = Modifier.fillMaxSize()) {
            SecondaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Yellow700,
                edgePadding = M_PADDING,
                indicator = {
                    Box(
                        modifier =
                            Modifier
                                .height(TAB_INDICATOR_HEIGHT)
                                .clip(
                                    RoundedCornerShape(
                                        TAB_INDICATOR_ROUND_CORNER_SIZE,
                                    ),
                                ).background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape =
                                        RoundedCornerShape(
                                            TAB_INDICATOR_ROUND_CORNER_SIZE,
                                        ),
                                ),
                    )
                },
            ) {
                statusList.forEachIndexed { index, status ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(
                                text = stringResource(status.textResId),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                fontFamily = helveticaFamily,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedContentColor = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            if (appointments.isEmpty()) {
                val message =
                    "${
                        stringResource(
                            R.string.feature_appointments_no_appointments_data,
                        )
                    }: ${stringResource(status.textResId)}"
                EmptyContentView(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(M_PADDING)
                            .verticalScroll(rememberScrollState()),
                    text = message,
                )
                return@Column
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(calculateGridColumns()),
                modifier = Modifier.fillMaxSize(),
                state = gridState,
                contentPadding = PaddingValues(all = S_PADDING),
                horizontalArrangement = Arrangement.spacedBy(S_PADDING),
                verticalArrangement = Arrangement.spacedBy(S_PADDING),
            ) {
                items(
                    appointments,
                    key = { appointment ->
                        appointment.id
                    },
                    span = { GridItemSpan(1) },
                ) { appointment ->
                    AppointmentCard(
                        doctorName = appointment.doctorName,
                        doctorSpecialty = appointment.doctorSpecialty,
                        dateTime = appointment.dateTime.getFormattedDateTime(LocalContext.current),
                        patientName = appointment.patientName,
                        statusId = appointment.statusId,
                        status = appointment.status,
                        showActions = showActions,
                        onReschedule = {
                            if (showActions) {
                                onIntent(AppointmentListIntent.RescheduleAppointment(appointment))
                            }
                        },
                        onCancel = {
                            if (showActions) {
                                onIntent(AppointmentListIntent.CancelAppointment(appointment))
                            }
                        },
                        onConfirm = {},
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
private fun MyAppointmentContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            MyAppointmentsContent(
                uiState =
                    AppointmentListState(
                        appointments =
                            listOf(
                                Appointment(
                                    id = 1,
                                    doctorName = "Dr. John Smith",
                                    doctorSpecialty = "Cardiologist",
                                    dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                                    patientName = "Patient",
                                    statusId = AppointmentStatus.PendingStatus.code,
                                    status = stringResource(AppointmentStatus.PendingStatus.textResId),
                                ),
                                Appointment(
                                    id = 2,
                                    doctorName = "Dr. Sarah Johnson",
                                    doctorSpecialty = "Dermatologist",
                                    dateTime = System.currentTimeMillis() + Constants.TWO_DAYS,
                                    patientName = "Patient",
                                    statusId = AppointmentStatus.PendingStatus.code,
                                    status = stringResource(AppointmentStatus.PendingStatus.textResId),
                                ),
                                Appointment(
                                    id = 3,
                                    doctorName = "Dr. Anna Jones",
                                    doctorSpecialty = "General Practitioner",
                                    dateTime = System.currentTimeMillis() + Constants.THREE_DAYS,
                                    patientName = "Patient",
                                    statusId = AppointmentStatus.PendingStatus.code,
                                    status = stringResource(AppointmentStatus.PendingStatus.textResId),
                                ),
                                Appointment(
                                    id = 4,
                                    doctorName = "Dr. Emanuel Johnson",
                                    doctorSpecialty = "Dermatologist",
                                    dateTime = System.currentTimeMillis() + Constants.FOUR_DAYS,
                                    patientName = "Patient",
                                    statusId = AppointmentStatus.PendingStatus.code,
                                    status = stringResource(AppointmentStatus.PendingStatus.textResId),
                                ),
                                Appointment(
                                    id = 5,
                                    doctorName = "Dr. Anna Jones",
                                    doctorSpecialty = "General Practitioner",
                                    dateTime = System.currentTimeMillis() + Constants.FIVE_DAYS,
                                    patientName = "Patient",
                                    statusId = AppointmentStatus.PendingStatus.code,
                                    status = stringResource(AppointmentStatus.PendingStatus.textResId),
                                ),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
