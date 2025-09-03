package eg.edu.cu.csds.icare.home.screen.home

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import eg.edu.cu.csds.icare.core.data.util.getFormattedDate
import eg.edu.cu.csds.icare.core.data.util.getFormattedTime
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Promotion
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.common.AppService
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.common.Role
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.ACTION_BUTTON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.ANNOUNCEMENT_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.CARD_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.DeepTeal
import eg.edu.cu.csds.icare.core.ui.theme.LightGreen
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Orange200
import eg.edu.cu.csds.icare.core.ui.theme.PROMOTION_BANNER_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.PROMOTION_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.U_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.statusColor
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.MediaHelper
import eg.edu.cu.csds.icare.core.ui.view.ConfirmDialog
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import eg.edu.cu.csds.icare.home.R
import eg.edu.cu.csds.icare.home.component.PromotionItem
import eg.edu.cu.csds.icare.home.component.ServiceItem
import eg.edu.cu.csds.icare.home.component.TopDoctorCard
import eg.edu.cu.csds.icare.home.component.UserTitleView
import eg.edu.cu.csds.icare.home.util.statusList
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.system.exitProcess

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToRoute: (Route) -> Unit,
    navigateToDoctorDetails: (Doctor) -> Unit,
) {
    val context: Context = LocalContext.current
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
                is HomeEffect.NavigateToDoctorDetails -> navigateToDoctorDetails(effect.doctor)
                is HomeEffect.ShowError -> {
                    alertMessage = effect.message.asString(context)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
                }
            }
        },
    )

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        if (uiState.openDialog) {
            ConfirmDialog(
                backgroundColor = backgroundColor,
                title = stringResource(id = R.string.feature_home_exit_dialog_title),
                message = stringResource(id = R.string.feature_home_exit_dialog),
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

        if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeState,
    appVersion: String,
    context: Context = LocalContext.current,
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

        Column(
            modifier =
                Modifier
                    .constrainAs(content) {
                        top.linkTo(line.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(marquee.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }.background(backgroundColor)
                    .verticalScroll(rememberScrollState())
                    .padding(M_PADDING),
        ) {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(PROMOTION_BANNER_HEIGHT),
                shape = RoundedCornerShape(CARD_ROUND_CORNER_SIZE),
                colors = CardDefaults.cardColors(containerColor = LightGreen),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(modifier = Modifier.padding(M_PADDING)) {
                        Text(
                            text = stringResource(R.string.feature_home_cosmetics),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = helveticaFamily,
                            color = DeepTeal,
                            maxLines = 1,
                        )
                        Text(
                            text = " ${stringResource(R.string.feature_home_discount_50)} ${
                                stringResource(R.string.feature_home_off)
                            }",
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = helveticaFamily,
                            color = Color.Gray,
                            maxLines = 1,
                        )
                        Button(
                            onClick = { },
                            modifier = Modifier.padding(top = S_PADDING),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = DeepTeal,
                                ),
                        ) {
                            Text(
                                text = stringResource(R.string.feature_home_buy_now),
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontFamily = helveticaFamily,
                                color = Color.White,
                                maxLines = 1,
                            )
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.feature_home_doctor_announcement),
                        contentDescription = null,
                        modifier = Modifier.size(ANNOUNCEMENT_IMAGE_SIZE),
                    )
                }
            }

            Spacer(modifier = Modifier.height(M_PADDING))

            Text(
                text = stringResource(R.string.feature_home_services),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = helveticaFamily,
                color = textColor,
                maxLines = 1,
            )
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = S_PADDING),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ServiceItem(
                    AppService.ScanCenter,
                    onClick = { onEvent(HomeEvent.NavigateToScanCentersScreen) },
                )
                ServiceItem(
                    AppService.BookAppointment,
                    onClick = { onEvent(HomeEvent.NavigateToBookAppointmentScreen) },
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ServiceItem(AppService.LabCenter, onClick = {
                    onEvent(HomeEvent.NavigateToLabCentersScreen)
                })
                ServiceItem(AppService.Pharmacy, onClick = {
                    onEvent(HomeEvent.NavigateToPharmaciesScreen)
                })
            }

            Spacer(modifier = Modifier.height(M_PADDING))

            Text(
                stringResource(R.string.feature_home_next_appointment),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = helveticaFamily,
                color = textColor,
                maxLines = 1,
            )

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(PROMOTION_ITEM_HEIGHT)
                        .clickable { onEvent(HomeEvent.NavigateToMyAppointmentsScreen) },
                shape = RoundedCornerShape(CARD_ROUND_CORNER_SIZE),
                colors =
                    CardDefaults.cardColors(
                        containerColor = cardBackgroundColor,
                    ),
            ) {
                ConstraintLayout(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(M_PADDING),
                ) {
                    val (
                        doctorImage, doctorName, doctorSpecialty, status, dateIcon, date,
                        timeIcon, time, message,
                    ) = createRefs()
                    uiState.myAppointments
                        .firstOrNull { appointment ->
                            appointment.statusId == AppointmentStatus.PendingStatus.code ||
                                appointment.statusId == AppointmentStatus.ConfirmedStatus.code
                        }?.let { nextAppointment ->
                            val appointment =
                                nextAppointment.copy(
                                    status =
                                        statusList
                                            .find { it.code == nextAppointment.statusId }
                                            ?.let {
                                                stringResource(it.textResId)
                                            }
                                            ?: stringResource(
                                                eg.edu.cu.csds.icare.core.ui.R.string.core_ui_undefined,
                                            ),
                                )

                            Box(
                                modifier =
                                    Modifier
                                        .constrainAs(doctorImage) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                        }.size(ACTION_BUTTON_SIZE),
                            ) {
                                AsyncImage(
                                    model = nextAppointment.doctorImage,
                                    contentDescription = null,
                                    placeholder =
                                        painterResource(
                                            eg.edu.cu.csds.icare.core.ui.R.drawable.core_ui_user_placeholder,
                                        ),
                                    modifier =
                                        Modifier
                                            .clip(RoundedCornerShape(M_PADDING)),
                                    error =
                                        painterResource(
                                            eg.edu.cu.csds.icare.core.ui.R.drawable.core_ui_user_placeholder,
                                        ),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            Text(
                                text = appointment.doctorName,
                                modifier =
                                    Modifier.constrainAs(doctorName) {
                                        top.linkTo(doctorImage.top)
                                        start.linkTo(
                                            doctorImage.end,
                                            margin = S_PADDING,
                                        )
                                    },
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                fontFamily = helveticaFamily,
                                color = textColor,
                                maxLines = 1,
                            )
                            Text(
                                text = appointment.doctorSpecialty,
                                modifier =
                                    Modifier.constrainAs(doctorSpecialty) {
                                        top.linkTo(doctorName.bottom)
                                        start.linkTo(doctorName.start)
                                        end.linkTo(doctorName.end)
                                    },
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontFamily = helveticaFamily,
                                color = textColor,
                                maxLines = 1,
                            )

                            Text(
                                text = appointment.status,
                                modifier =
                                    Modifier.constrainAs(status) {
                                        top.linkTo(doctorName.top)
                                        end.linkTo(parent.end)
                                    },
                                color =
                                    if (appointment.statusId ==
                                        AppointmentStatus.ConfirmedStatus.code
                                    ) {
                                        Color.Green
                                    } else {
                                        statusColor
                                    },
                            )
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                modifier =
                                    Modifier.constrainAs(dateIcon) {
                                        top.linkTo(
                                            doctorImage.bottom,
                                            margin = XS_PADDING,
                                        )
                                        start.linkTo(doctorImage.start)
                                    },
                            )
                            Text(
                                text = appointment.dateTime.getFormattedDate(context),
                                modifier =
                                    Modifier.constrainAs(date) {
                                        top.linkTo(dateIcon.top)
                                        start.linkTo(dateIcon.end, margin = U_PADDING)
                                        bottom.linkTo(dateIcon.bottom)
                                    },
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontFamily = helveticaFamily,
                                color = textColor,
                                maxLines = 1,
                            )

                            Icon(
                                painterResource(R.drawable.feature_home_baseline_access_time_24),
                                contentDescription = null,
                                modifier =
                                    Modifier.constrainAs(timeIcon) {
                                        top.linkTo(dateIcon.top)
                                        end.linkTo(time.start, margin = U_PADDING)
                                    },
                            )

                            Text(
                                text = appointment.dateTime.getFormattedTime(context),
                                modifier =
                                    Modifier.constrainAs(time) {
                                        top.linkTo(timeIcon.top)
                                        end.linkTo(status.end)
                                        bottom.linkTo(timeIcon.bottom)
                                    },
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontFamily = helveticaFamily,
                                color = textColor,
                                maxLines = 1,
                            )
                        } ?: run {
                        Text(
                            text = stringResource(R.string.feature_home_no_upcoming_appointments),
                            modifier =
                                Modifier.constrainAs(message) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                },
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = helveticaFamily,
                            color = Color.Gray,
                            maxLines = 1,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(M_PADDING))

            Text(
                stringResource(R.string.feature_home_promotions),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = helveticaFamily,
                color = textColor,
                maxLines = 1,
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                items(uiState.promotions, key = { promotion ->
                    promotion.id
                }) { promotion ->
                    PromotionItem(promotion)
                }
            }

            Spacer(modifier = Modifier.height(M_PADDING))

            Text(
                stringResource(R.string.feature_home_top_doctors),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = helveticaFamily,
                color = textColor,
                maxLines = 1,
            )
            LazyRow {
                items(uiState.topDoctors) { doctor ->
                    TopDoctorCard(
                        doctor,
                        modifier =
                            Modifier.clickable {
                                onEvent(HomeEvent.NavigateToDoctorDetails(doctor))
                            },
                    )
                }
            }
        }

        Text(
            text = stringResource(id = eg.edu.cu.csds.icare.core.ui.R.string.core_ui_made_by),
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
                    myAppointments =
                        listOf(
                            Appointment(
                                appointmentId = 1,
                                doctorName = "Dr. Ahmed Gad",
                                doctorSpecialty = "Cardiologist",
                                doctorImage = "https://i.ibb.co/JRkcZzhR/doctor.webp",
                                dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                                statusId = AppointmentStatus.ConfirmedStatus.code,
                            ),
                        ),
                    topDoctors =
                        listOf(
                            Doctor(
                                name = "Dr. Anna Jones",
                                specialty = "General Practitioner",
                                rating = 4.5,
                            ),
                            Doctor(
                                name = "Dr. John Berry",
                                specialty = "General Practitioner",
                                rating = 4.5,
                            ),
                            Doctor(
                                name = "Dr. Atiyah",
                                specialty = "General Practitioner",
                                rating = 4.5,
                            ),
                        ),
                    promotions =
                        listOf(
                            Promotion(
                                id = 1,
                                imageUrl = "https://i.postimg.cc/5jjyk7Jn/promo1.png",
                                discount = stringResource(R.string.feature_home_discount_30),
                            ),
                            Promotion(
                                id = 2,
                                imageUrl = "https://i.postimg.cc/vDjTRrHM/promo2.png",
                                discount = stringResource(R.string.feature_home_discount_50),
                            ),
                        ),
                ),
            appVersion = "1.0.0",
            onEvent = {},
        )
    }
}
