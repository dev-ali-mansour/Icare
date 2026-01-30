package eg.edu.cu.csds.icare.feature.appointment.screen.reschedule

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.util.getAvailableTimeSlots
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.CommonTopAppBar
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.RECORD_PATIENT_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow300
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import eg.edu.cu.csds.icare.feature.appointment.R
import eg.edu.cu.csds.icare.feature.appointment.component.TimeSlotSelector
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AppointmentRescheduleScreen(onNavigationIconClicked: () -> Unit) {
    val viewModel: RescheduleViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(RescheduleIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is RescheduleEffect.ShowSuccess -> {
                    showSuccessDialog = true
                    delay(timeMillis = 3000)
                    onNavigationIconClicked()
                }

                is RescheduleEffect.ShowError -> {
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
                title = stringResource(id = R.string.feature_appointments_appointment_reschedule),
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
                            viewModel.handleIntent(RescheduleIntent.Refresh)
                        },
                    ).padding(innerPadding),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
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
                AppointmentRescheduleContent(
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
private fun AppointmentRescheduleContent(
    uiState: RescheduleState,
    modifier: Modifier = Modifier,
    onIntent: (RescheduleIntent) -> Unit,
) {
    val context: Context = LocalContext.current
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = M_PADDING),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = XS_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            uiState.appointment?.let { appointment ->
                uiState.doctorSchedule?.let { schedule ->
                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .heightIn(min = RECORD_PATIENT_CARD_HEIGHT)
                                .padding(vertical = XS_PADDING),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(XS_PADDING),
                    ) {
                        ConstraintLayout(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(XL_PADDING),
                        ) {
                            val (image, name, speciality, price, totalApp, rating) = createRefs()
                            Image(
                                modifier =
                                    Modifier
                                        .padding(XS_PADDING)
                                        .clip(CircleShape)
                                        .border(
                                            BOARDER_SIZE,
                                            Color.DarkGray,
                                            CircleShape,
                                        ).size(PROFILE_IMAGE_SIZE)
                                        .constrainAs(image) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        },
                                painter =
                                    rememberAsyncImagePainter(
                                        ImageRequest
                                            .Builder(context)
                                            .data(data = appointment.doctorImage)
                                            .placeholder(
                                                drawable.core_ui_user_placeholder,
                                            ).error(
                                                drawable.core_ui_user_placeholder,
                                            ).build(),
                                    ),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                            )

                            Text(
                                text = "${
                                    stringResource(
                                        string.core_ui_name,
                                    )
                                }: ${appointment.doctorName}",
                                modifier =
                                    Modifier.constrainAs(name) {
                                        top.linkTo(image.bottom, margin = S_PADDING)
                                        start.linkTo(parent.start)
                                        width = Dimension.fillToConstraints
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                textAlign = TextAlign.Start,
                                fontFamily = helveticaFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                            )

                            Text(
                                text = "${
                                    stringResource(
                                        string.core_ui_speciality,
                                    )
                                }: ${appointment.doctorSpecialty}",
                                modifier =
                                    Modifier.constrainAs(speciality) {
                                        top.linkTo(name.bottom)
                                        start.linkTo(name.start)
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                textAlign = TextAlign.Start,
                                fontFamily = helveticaFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                            )

                            Text(
                                text = "${
                                    stringResource(
                                        string.core_ui_appointment_price,
                                    )
                                }: ${schedule.price} ${
                                    stringResource(
                                        string.core_ui_egp,
                                    )
                                }",
                                modifier =
                                    Modifier.constrainAs(price) {
                                        top.linkTo(speciality.bottom)
                                        start.linkTo(name.start)
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                textAlign = TextAlign.Start,
                                fontFamily = helveticaFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                            )

                            Text(
                                text = "${
                                    stringResource(
                                        string.core_ui_total_appointments,
                                    )
                                }: ${schedule.totalPatients}",
                                modifier =
                                    Modifier.constrainAs(totalApp) {
                                        top.linkTo(price.bottom)
                                        start.linkTo(name.start)
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                textAlign = TextAlign.Start,
                                fontFamily = helveticaFamily,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                            )

                            Row(
                                modifier =
                                    Modifier.constrainAs(rating) {
                                        top.linkTo(totalApp.bottom)
                                        start.linkTo(name.start)
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "${
                                        stringResource(
                                            string.core_ui_rating,
                                        )
                                    }: ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    textAlign = TextAlign.Start,
                                    fontFamily = helveticaFamily,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                )
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Yellow300,
                                    modifier = Modifier.size(M_PADDING),
                                )
                                Text(
                                    text = "${schedule.rating}",
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(S_PADDING))

                    val availableSlots =
                        getAvailableTimeSlots(
                            from = schedule.fromTime,
                            to = schedule.toTime,
                            bookedAppointments = schedule.appointments,
                        )

                    TimeSlotSelector(
                        slots = availableSlots,
                        selectedSlot = appointment.dateTime,
                        onSlotSelected = { onIntent(RescheduleIntent.UpdateSelectedSlot(it)) },
                    )

                    Spacer(modifier = Modifier.height(L_PADDING))

                    AnimatedButton(
                        modifier =
                            Modifier
                                .fillMaxWidth(fraction = 0.6f),
                        text = stringResource(string.core_ui_reschedule),
                        color = buttonBackgroundColor,
                        onClick = {
                            onIntent(RescheduleIntent.Proceed)
                        },
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
private fun AppointmentRescheduleContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            AppointmentRescheduleContent(
                uiState =
                    RescheduleState(
                        appointment =
                            Appointment(
                                doctorName = "Dr. John Doe",
                                doctorImage = "https://i.ibb.co/JRkcZzhR/doctor.webp",
                                dateTime = System.currentTimeMillis().plus(other = 30 * 60 * 1000),
                            ),
                        doctorSchedule =
                            DoctorSchedule(
                                fromTime = System.currentTimeMillis(),
                                toTime = System.currentTimeMillis().plus(other = 5 * 60 * 60 * 1000),
                                appointments = listOf(),
                            ),
                    ),
                onIntent = {},
            )
        }
    }
}
