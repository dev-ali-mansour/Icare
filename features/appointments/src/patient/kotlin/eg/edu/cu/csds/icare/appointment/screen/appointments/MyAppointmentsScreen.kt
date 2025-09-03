package eg.edu.cu.csds.icare.appointment.screen.appointments

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.appointment.component.AppointmentCard
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isTomorrow
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    viewModel: AppointmentListViewModel = koinViewModel(),
    onNavigationIconClicked: () -> Unit,
    navigateToRescheduleRoute: (Appointment) -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(AppointmentListEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is AppointmentListEffect.OnBackClick -> onNavigationIconClicked()
                is AppointmentListEffect.NavigateToRescheduleAppointment ->
                    navigateToRescheduleRoute(
                        effect.appointment,
                    )
                is AppointmentListEffect.ShowSuccess -> {
                    showSuccessDialog = true
                    delay(timeMillis = 3000)
                    showSuccessDialog = false
                }
                is AppointmentListEffect.ShowError -> {
                    alertMessage = effect.message.asString(context)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
                }
            }
        },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.features_appointments_my_appointments)) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = barBackgroundColor,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                navigationIcon = {
                    IconButton(onClick = { onNavigationIconClicked() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.processEvent(AppointmentListEvent.Refresh)
                        },
                    ).padding(paddingValues),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .background(backgroundColor)
                        .fillMaxWidth(),
            ) {
                val (line, content, refresh) = createRefs()
                Box(
                    modifier =
                        Modifier
                            .constrainAs(line) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }.background(Yellow500)
                            .fillMaxWidth()
                            .height(XS_PADDING),
                )

                MyAppointmentsContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(line.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    uiState = uiState,
                    onEvent = viewModel::processEvent,
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
                if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
            }
        }
    }
}

@Composable
fun MyAppointmentsContent(
    uiState: AppointmentListState,
    modifier: Modifier = Modifier,
    onEvent: (AppointmentListEvent) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val statusList =
            listOf(
                AppointmentStatus.PendingStatus,
                AppointmentStatus.ConfirmedStatus,
                AppointmentStatus.CompletedStatus,
                AppointmentStatus.CancelledStatus,
            )
        val (card) = createRefs()

        Surface(
            modifier =
                Modifier
                    .constrainAs(card) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
        ) {
            val appointments =
                uiState.appointments.map { appointment ->
                    statusList.find { it.code == appointment.statusId }?.let {
                        appointment.copy(status = stringResource(it.textResId))
                    } ?: appointment
                }
            Column(modifier = Modifier.fillMaxSize()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = contentBackgroundColor,
                    contentColor = Yellow700,
                    edgePadding = M_PADDING,
                    indicator = { tabPositions ->
                        Box(
                            modifier =
                                Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    .height(TAB_INDICATOR_HEIGHT)
                                    .clip(
                                        RoundedCornerShape(
                                            TAB_INDICATOR_ROUND_CORNER_SIZE,
                                        ),
                                    ).background(
                                        color = barBackgroundColor,
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
                                    color = barBackgroundColor,
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    fontFamily = helveticaFamily,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                )
                            },
                            selectedContentColor = barBackgroundColor,
                            unselectedContentColor = barBackgroundColor,
                        )
                    }
                }

                when (selectedTabIndex) {
                    statusList.indexOf(AppointmentStatus.PendingStatus) ->
                        UpcomingAppointmentsContent(
                            appointments = appointments,
                            onReschedule = { onEvent(AppointmentListEvent.RescheduleAppointment(it)) },
                            onCancel = {
                                onEvent(AppointmentListEvent.CancelAppointment(it))
                            },
                        )

                    statusList.indexOf(AppointmentStatus.ConfirmedStatus),
                    statusList.indexOf(AppointmentStatus.CompletedStatus),
                    statusList.indexOf(AppointmentStatus.CancelledStatus),
                    ->
                        OtherAppointmentsContent(
                            status = statusList[selectedTabIndex],
                            appointments =
                                appointments.filter {
                                    it.statusId == statusList[selectedTabIndex].code
                                },
                        )
                }
            }
        }
    }
}

@Composable
private fun UpcomingAppointmentsContent(
    appointments: List<Appointment>,
    onReschedule: (Appointment) -> Unit,
    onCancel: (Appointment) -> Unit,
) {
    if (appointments.none { it.statusId == AppointmentStatus.PendingStatus.code }) {
        val message =
            "${stringResource(
                R.string.features_appointments_no_appointments_data,
            )}: ${stringResource(AppointmentStatus.PendingStatus.textResId)}"
        EmptyContentView(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(M_PADDING)
                    .verticalScroll(rememberScrollState()),
            text = message,
        )
        return
    }
    val tomorrowAppointments =
        appointments.filter {
            it.statusId == AppointmentStatus.PendingStatus.code &&
                it.dateTime.isTomorrow()
        }

    val pendingAppointments =
        appointments.filter {
            it.statusId == AppointmentStatus.PendingStatus.code && !it.dateTime.isTomorrow()
        }

    LazyColumn(
        modifier = Modifier.padding(M_PADDING),
        verticalArrangement = Arrangement.spacedBy(S_PADDING),
    ) {
        if (tomorrowAppointments.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.features_appointments_tomorrow),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = helveticaFamily,
                    modifier = Modifier.padding(vertical = S_PADDING),
                )
            }

            items(tomorrowAppointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    showActions = true,
                    onReschedule = { onReschedule(appointment) },
                    onCancel = { onCancel(appointment) },
                    onConfirm = {},
                )
            }
        }

        if (pendingAppointments.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.features_appointments_pending_for_confirmation),
                    fontFamily = helveticaFamily,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = S_PADDING),
                )
            }

            items(pendingAppointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    showActions = true,
                    onReschedule = { onReschedule(appointment) },
                    onCancel = { onCancel(appointment) },
                    onConfirm = {},
                )
            }
        }
    }
}

@Composable
private fun OtherAppointmentsContent(
    status: AppointmentStatus,
    appointments: List<Appointment>,
) {
    if (appointments.isEmpty()) {
        val message =
            "${stringResource(
                R.string.features_appointments_no_appointments_data,
            )}: ${stringResource(status.textResId)}"
        EmptyContentView(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(M_PADDING)
                    .verticalScroll(rememberScrollState()),
            text = message,
        )
        return
    }
    LazyColumn(
        modifier = Modifier.padding(M_PADDING),
        verticalArrangement = Arrangement.spacedBy(S_PADDING),
    ) {
        items(appointments) { appointment ->
            AppointmentCard(
                appointment = appointment,
                showActions = false,
                onReschedule = {},
                onCancel = {},
                onConfirm = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
private fun MyAppointmentContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        MyAppointmentsContent(
            uiState =
                AppointmentListState(
                    appointments =
                        listOf(
                            Appointment(
                                appointmentId = 1,
                                doctorName = "Dr. John Smith",
                                doctorSpecialty = "Cardiologist",
                                doctorId = "101",
                                doctorImage = "",
                                dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                                patientName = "Patient",
                                patientImage = "",
                                statusId = AppointmentStatus.PendingStatus.code,
                                status = stringResource(AppointmentStatus.PendingStatus.textResId),
                            ),
                            Appointment(
                                appointmentId = 2,
                                doctorName = "Dr. Sarah Johnson",
                                doctorSpecialty = "Dermatologist",
                                doctorId = "102",
                                doctorImage = "",
                                dateTime = System.currentTimeMillis() + Constants.THREE_DAYS,
                                patientName = "Patient",
                                patientImage = "",
                                statusId = AppointmentStatus.PendingStatus.code,
                                status = stringResource(AppointmentStatus.PendingStatus.textResId),
                            ),
                            Appointment(
                                appointmentId = 3,
                                doctorName = "Dr. Anna Jones",
                                doctorSpecialty = "General Practitioner",
                                doctorId = "101",
                                doctorImage = "",
                                dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                                patientName = "Patient",
                                patientImage = "",
                                statusId = AppointmentStatus.PendingStatus.code,
                                status = stringResource(AppointmentStatus.PendingStatus.textResId),
                            ),
                        ),
                ),
            onEvent = {},
        )
    }
}
