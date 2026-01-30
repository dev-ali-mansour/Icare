package eg.edu.cu.csds.icare.feature.appointment.screen.booking

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import eg.edu.cu.csds.icare.core.domain.model.Doctor
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
internal fun BookingScreen(
    onNavigationIconClicked: () -> Unit,
    onSuccess: () -> Unit,
) {
    val viewModel: BookingViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val refreshState = rememberPullToRefreshState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(BookingIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is BookingEffect.ShowSuccess -> {
                    showSuccessDialog = true
                    delay(timeMillis = 3000)
                    onSuccess()
                }

                is BookingEffect.ShowError -> {
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
                title = stringResource(R.string.feature_appointments_booking),
            ) { onNavigationIconClicked() }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Surface(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isLoading,
                        onRefresh = {
                            viewModel.handleIntent(BookingIntent.Refresh)
                        },
                    ),
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (content, refresh) = createRefs()

                BookingContent(
                    modifier =
                        Modifier.constrainAs(content) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    doctor = uiState.doctor,
                    doctorSchedule = uiState.doctorSchedule,
                    selectedSlot = uiState.selectedSlot,
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
private fun BookingContent(
    doctor: Doctor?,
    doctorSchedule: DoctorSchedule?,
    selectedSlot: Long,
    modifier: Modifier = Modifier,
    onIntent: (BookingIntent) -> Unit,
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
            doctor?.let { doctor ->
                doctorSchedule?.let { schedule ->
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
                                            .data(data = doctor.profilePicture)
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
                                }: ${doctor.name}",
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
                                text = "${stringResource(string.core_ui_speciality)}: ${doctor.specialty}",
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
                                }: ${doctor.price} ${
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
                                    text = "${doctor.rating}",
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(S_PADDING))

                    val availableSlots =
                        getAvailableTimeSlots(
                            from = doctor.fromTime,
                            to = doctor.toTime,
                            bookedAppointments = schedule.appointments,
                        )

                    TimeSlotSelector(
                        slots = availableSlots,
                        selectedSlot = selectedSlot,
                        onSlotSelected = { onIntent(BookingIntent.UpdateSelectedSlot(it)) },
                    )

                    Spacer(modifier = Modifier.height(L_PADDING))

                    AnimatedButton(
                        modifier =
                            Modifier
                                .fillMaxWidth(fraction = 0.6f),
                        text = stringResource(string.core_ui_book_now),
                        color = buttonBackgroundColor,
                        onClick = {
                            onIntent(BookingIntent.Proceed)
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
private fun BookingContentPreview() {
    IcareTheme {
        val doctor =
            Doctor(
                name = "Dr. John Doe",
                specialty = "Cardiology",
                price = 500.0,
                fromTime = System.currentTimeMillis(),
                toTime = System.currentTimeMillis().plus(other = 5 * 60 * 60 * 1000),
            )
        val schedule = DoctorSchedule(appointments = listOf(), totalPatients = 1000)
        val selectedSlot =
            getAvailableTimeSlots(
                from = doctor.fromTime,
                to = doctor.toTime,
                bookedAppointments = schedule.appointments,
            ).first()

        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            BookingContent(
                doctor = doctor,
                doctorSchedule = schedule,
                selectedSlot = selectedSlot,
                onIntent = {},
            )
        }
    }
}
