package eg.edu.cu.csds.icare.appointment.screen

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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.util.getAvailableTimeSlots
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.RECORD_PATIENT_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow300
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun DoctorProfileContent(
    userRes: Resource<User>,
    doctor: Doctor,
    doctorScheduleRes: Resource<DoctorSchedule>,
    actionResource: Resource<Nothing?>,
    showLoading: (Boolean) -> Unit,
    selectedSlot: Long,
    onSlotSelected: (Long) -> Unit,
    onProceedButtonClicked: (String, String) -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = M_PADDING),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (card, details) = createRefs()
            userRes.data?.let { user ->
                when (doctorScheduleRes) {
                    is Resource.Unspecified -> LaunchedEffect(key1 = true) { showLoading(false) }
                    is Resource.Loading -> LaunchedEffect(key1 = true) { showLoading(true) }

                    is Resource.Success -> {
                        LaunchedEffect(key1 = true) { showLoading(false) }
                        doctorScheduleRes.data?.let { schedule ->
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
                                                .border(BOARDER_SIZE, Color.DarkGray, CircleShape)
                                                .size(PROFILE_IMAGE_SIZE)
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
                                                    .placeholder(CoreR.drawable.user_placeholder)
                                                    .error(CoreR.drawable.user_placeholder)
                                                    .build(),
                                            ),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                    )

                                    Text(
                                        text = "${stringResource(CoreR.string.name)}: ${doctor.name}",
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
                                        text = "${stringResource(CoreR.string.speciality)}: ${doctor.specialty}",
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
                                        text = "${stringResource(CoreR.string.appointment_price)}: ${doctor.price} ${
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
                                        text = "${stringResource(CoreR.string.total_appointments)}: ${schedule.totalPatients}",
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
                                            text = "${doctor.rating}",
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
                                        }.verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                val availableSlots =
                                    getAvailableTimeSlots(
                                        from = doctor.fromTime,
                                        to = doctor.toTime,
                                        bookedAppointments = schedule.appointments,
                                    )

                                TimeSlotSelector(
                                    dates = availableSlots,
                                    selectedSlot = selectedSlot,
                                    onSlotSelected = { onSlotSelected(it) },
                                )

                                Spacer(modifier = Modifier.height(L_PADDING))

                                AnimatedButton(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth(fraction = 0.6f),
                                    text = stringResource(CoreR.string.book_now),
                                    color = buttonBackgroundColor,
                                    onClick = { onProceedButtonClicked(doctor.id, user.userId) },
                                )
                            }
                        }
                    }

                    is Resource.Error ->
                        LaunchedEffect(key1 = true) {
                            showLoading(false)
                            onError(doctorScheduleRes.error)
                        }
                }
            }

            when (actionResource) {
                is Resource.Unspecified -> LaunchedEffect(key1 = true) { showLoading(false) }
                is Resource.Loading -> LaunchedEffect(key1 = true) { showLoading(true) }

                is Resource.Success ->
                    LaunchedEffect(key1 = Unit) {
                        showLoading(false)
                        onSuccess()
                    }

                is Resource.Error ->
                    LaunchedEffect(key1 = true) {
                        showLoading(false)
                        onError(actionResource.error)
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
internal fun DoctorDetailsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        DoctorProfileContent(
            userRes = Resource.Success(data = User()),
            doctor =
                Doctor(
                    name = "Dr. John Doe",
                    profilePicture = "https://t4.ftcdn.net/jpg/01/98/82/75/360_F_198827520_wVNNHdMq4yLJe76WWivQQ5Ev2WtXac4N.webp",
                    fromTime = System.currentTimeMillis(),
                    toTime = System.currentTimeMillis().plus(5 * 60 * 60 * 1000),
                ),
            doctorScheduleRes = Resource.Success(DoctorSchedule(appointments = listOf())),
            actionResource = Resource.Unspecified(),
            showLoading = {},
            selectedSlot = System.currentTimeMillis().plus(30 * 60 * 1000),
            onSlotSelected = {},
            onProceedButtonClicked = { _, _ -> },
            onSuccess = {},
            onError = {},
        )
    }
}
