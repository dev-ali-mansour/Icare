package eg.edu.cu.csds.icare.home.screen.home

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.common.Role
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Orange200
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.core.ui.view.ConfirmDialog
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.home.R
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.home.component.UserTitleView
import eg.edu.cu.csds.icare.home.screen.admin.AdminContent
import eg.edu.cu.csds.icare.home.screen.clinic.ClinicianContent
import eg.edu.cu.csds.icare.home.screen.doctor.DoctorContent
import eg.edu.cu.csds.icare.home.util.statusList
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToRoute: (Route) -> Unit,
    navigateToUpdateDoctorScreen: (Doctor) -> Unit,
    navigateToNewConsultation: (Appointment) -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    val mediaHelper: MediaHelper = koinInject()
    val appVersion: String =
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName ?: ""

    BackHandler {
        viewModel.processEvent(HomeEvent.UpdateOpenDialog(isOpen = true))
    }

    LaunchedEffect(key1 = mediaHelper.isGreetingPlayed) {
        if (!mediaHelper.isGreetingPlayed) {
            mediaHelper.play(R.raw.feature_home_welcome)
        }
    }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(HomeEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is HomeEffect.NavigateToRoute -> navigateToRoute(effect.route)
                is HomeEffect.NavigateToUpdateDoctorScreen -> navigateToUpdateDoctorScreen(effect.doctor)
                is HomeEffect.NavigateToNewConsultation -> navigateToNewConsultation(effect.appointment)

                is HomeEffect.ShowError -> {
                    alertMessage = effect.message.asString(context)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
                }
            }
        },
    )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .pullToRefresh(
                    state = refreshState,
                    isRefreshing = uiState.isLoading,
                    onRefresh = {
                        viewModel.processEvent(HomeEvent.Refresh)
                    },
                ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            if (uiState.openDialog) {
                ConfirmDialog(
                    backgroundColor = backgroundColor,
                    title =
                        stringResource(
                            id = string.core_ui_exit_dialog_title,
                        ),
                    message =
                        stringResource(
                            id = string.core_ui_exit_dialog,
                        ),
                    onDismissRequest = {
                        viewModel.processEvent(HomeEvent.UpdateOpenDialog(isOpen = false))
                    },
                    onConfirmed = {
                        viewModel.processEvent(HomeEvent.UpdateOpenDialog(isOpen = false))
                        (context as Activity).finish()
                        exitProcess(0)
                    },
                    onCancelled = {
                        viewModel.processEvent(HomeEvent.UpdateOpenDialog(isOpen = false))
                    },
                )
            }

            HomeContent(
                uiState = uiState,
                appVersion = appVersion,
                onEvent = viewModel::processEvent,
            )
        }

        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = uiState.isLoading,
            state = refreshState,
        )

        if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeState,
    appVersion: String,
    onEvent: (HomeEvent) -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (titleContainer, line, content, marquee) = createRefs()
        Surface(
            modifier =
                Modifier.constrainAs(titleContainer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            color = backgroundColor,
            tonalElevation = S_PADDING,
        ) {
            UserTitleView(
                modifier = Modifier,
                appVersion = appVersion,
                user = uiState.currentUser,
                onUserClicked = { onEvent(HomeEvent.NavigateToProfileScreen) },
            )
        }

        Box(
            modifier =
                Modifier
                    .constrainAs(line) {
                        top.linkTo(titleContainer.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }.fillMaxWidth()
                    .height(XS_PADDING)
                    .background(Yellow500),
        )
        uiState.currentUser?.let { user ->
            when (user.roleId) {
                Role.AdminRole.code -> {
                    uiState.adminStatistics?.let { stats ->
                        AdminContent(
                            modifier =
                                Modifier.constrainAs(content) {
                                    top.linkTo(line.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(marquee.top, margin = M_PADDING)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                },
                            stats = stats,
                            onSectionsAdminClicked = {
                                onEvent(HomeEvent.NavigateToSectionsAdminScreen)
                            },
                        )
                    }
                }

                Role.DoctorRole.code -> {
                    uiState.doctorSchedule?.let { schedule ->
                        DoctorContent(
                            modifier =
                                Modifier.constrainAs(content) {
                                    top.linkTo(line.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(marquee.top, margin = M_PADDING)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                },
                            schedule = schedule,
                            onPriceCardClicked = {
                                uiState.currentDoctor?.let { doctor ->
                                    onEvent(HomeEvent.NavigateToUpdateDoctorScreen(doctor))
                                }
                            },
                            onAppointmentClick = { onEvent(HomeEvent.NavigateToNewConsultation(it)) },
                            onSeeAllClick = { onEvent(HomeEvent.NavigateToAppointmentsScreen) },
                        )
                    }
                }

                Role.ClinicianRole.code -> {
                    ClinicianContent(
                        modifier =
                            Modifier.constrainAs(content) {
                                top.linkTo(line.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(marquee.top, margin = M_PADDING)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            },
                        appointments =
                            uiState.appointments.map { appointment ->
                                statusList.find { it.code == appointment.statusId }?.let {
                                    appointment.copy(status = stringResource(it.textResId))
                                } ?: appointment
                            },
                        onConfirm = { onEvent(HomeEvent.ConfirmAppointment(it)) },
                    )
                }

                else -> Unit
            }
        }

        Text(
            text = stringResource(id = string.core_ui_made_by),
            modifier =
                Modifier
                    .constrainAs(marquee) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }.fillMaxWidth(fraction = 0.9f)
                    .basicMarquee(iterations = Int.MAX_VALUE),
            color = Orange200,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = helveticaFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentPreview() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(XS_PADDING),
    ) {
        HomeContent(
            uiState =
                HomeState(
                    currentUser =
                        User(
                            roleId = Role.AdminRole.code,
                            displayName = "Ali Mansour",
                            email = "",
                            photoUrl = "",
                        ),
                    adminStatistics =
                        AdminStatistics(
                            totalUsers = 2500,
                            doctors = 150,
                            pharmacies = 35,
                            scanCenters = 25,
                            labCenters = 15,
                            pending = 4590,
                            confirmed = 178,
                            completed = 2850,
                            cancelled = 12,
                        ),
                    doctorSchedule =
                        DoctorSchedule(
                            totalPatients = 2000,
                            confirmed = 16,
                            price = 500.0,
                            availableSlots = 5,
                            appointments =
                                listOf(
                                    Appointment(
                                        patientName = "محمد السيد عثمان",
                                        dateTime = System.currentTimeMillis(),
                                    ),
                                    Appointment(
                                        patientName = "ابراهيم محمد",
                                        dateTime = System.currentTimeMillis(),
                                    ),
                                    Appointment(
                                        patientName = "شاكر محمد العربي",
                                        dateTime = System.currentTimeMillis(),
                                    ),
                                    Appointment(
                                        patientName = "أحمد عبد الحليم مهران",
                                        dateTime = System.currentTimeMillis(),
                                    ),
                                ),
                        ),
                ),
            appVersion = "1.0.0",
            onEvent = {},
        )
    }
}
