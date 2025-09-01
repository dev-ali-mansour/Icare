package eg.edu.cu.csds.icare.appointment.screen.reschedule

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.appointment.screen.TimeSlotSelector
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.util.getAvailableTimeSlots
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.RECORD_PATIENT_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow300
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.core.ui.view.SuccessesDialog
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppointmentRescheduleScreen(
    viewModel: RescheduleViewModel = koinViewModel(),
    onNavigationIconClicked: () -> Unit,
) {
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.processEvent(RescheduleEvent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is RescheduleEffect.ShowSuccess -> {
                    showSuccessDialog = true
                    delay(timeMillis = 3000)
                    onNavigationIconClicked()
                }

                is RescheduleEffect.ShowError -> {
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
                title = { Text(text = stringResource(id = R.string.appointment_reschedule)) },
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
                            viewModel.processEvent(RescheduleEvent.Refresh)
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
private fun AppointmentRescheduleContent(
    uiState: RescheduleState,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onEvent: (RescheduleEvent) -> Unit,
) {
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = M_PADDING),
    ) {
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            val (card, details) = createRefs()
            uiState.appointment?.let { appointment ->
                uiState.doctorSchedule?.let { schedule ->
                    Card(
                        modifier =
                            Modifier
                                .constrainAs(card) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }.height(RECORD_PATIENT_CARD_HEIGHT)
                                .padding(XS_PADDING),
                        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
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
                                            .placeholder(CoreR.drawable.user_placeholder)
                                            .error(CoreR.drawable.user_placeholder)
                                            .build(),
                                    ),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                            )

                            Text(
                                text = "${stringResource(CoreR.string.name)}: ${appointment.doctorName}",
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
                                color = Color.White,
                                maxLines = 1,
                            )

                            Text(
                                text = "${stringResource(
                                    CoreR.string.speciality,
                                )}: ${appointment.doctorSpecialty}",
                                modifier =
                                    Modifier.constrainAs(speciality) {
                                        top.linkTo(name.bottom)
                                        start.linkTo(name.start)
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                textAlign = TextAlign.Start,
                                fontFamily = helveticaFamily,
                                color = Color.White,
                                maxLines = 1,
                            )

                            Text(
                                text = "${stringResource(
                                    CoreR.string.appointment_price,
                                )}: ${schedule.price} ${
                                    stringResource(
                                        CoreR.string.egp,
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
                                color = Color.White,
                                maxLines = 1,
                            )

                            Text(
                                text = "${stringResource(
                                    CoreR.string.total_appointments,
                                )}: ${schedule.totalPatients}",
                                modifier =
                                    Modifier.constrainAs(totalApp) {
                                        top.linkTo(price.bottom)
                                        start.linkTo(name.start)
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                textAlign = TextAlign.Start,
                                fontFamily = helveticaFamily,
                                color = Color.White,
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
                                    text = "${stringResource(CoreR.string.rating)}: ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    textAlign = TextAlign.Start,
                                    fontFamily = helveticaFamily,
                                    color = Color.White,
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

                    Column(
                        modifier =
                            Modifier
                                .constrainAs(details) {
                                    top.linkTo(card.bottom, margin = S_PADDING)
                                    start.linkTo(card.start)
                                    end.linkTo(card.end)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val availableSlots =
                            getAvailableTimeSlots(
                                from = schedule.fromTime,
                                to = schedule.toTime,
                                bookedAppointments = schedule.appointments,
                            )

                        TimeSlotSelector(
                            dates = availableSlots,
                            selectedSlot = appointment.dateTime,
                            onSlotSelected = { onEvent(RescheduleEvent.UpdateSelectedSlot(it)) },
                        )

                        Spacer(modifier = Modifier.height(L_PADDING))

                        AnimatedButton(
                            modifier =
                                Modifier
                                    .fillMaxWidth(fraction = 0.6f),
                            text = stringResource(CoreR.string.reschedule),
                            color = buttonBackgroundColor,
                            onClick = {
                                onEvent(RescheduleEvent.Proceed)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
private fun AppointmentRescheduleContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        AppointmentRescheduleContent(
            uiState =
                RescheduleState(
                    appointment =
                        Appointment(
                            doctorName = "Dr. John Doe",
                            doctorImage = "https://i.ibb.co/JRkcZzhR/doctor.webp",
                            dateTime = System.currentTimeMillis(), // .plus(other = 30 * 60 * 1000),
                        ),
                    doctorSchedule =
                        DoctorSchedule(
                            fromTime = System.currentTimeMillis(),
                            toTime = System.currentTimeMillis().plus(other = 5 * 60 * 60 * 1000),
                            appointments = listOf(),
                        ),
                ),
            onEvent = {},
        )
    }
}
