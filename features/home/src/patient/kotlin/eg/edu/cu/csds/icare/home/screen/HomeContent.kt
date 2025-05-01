package eg.edu.cu.csds.icare.home.screen

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Promotion
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.common.AppService
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.common.Role
import eg.edu.cu.csds.icare.core.ui.navigation.Screen
import eg.edu.cu.csds.icare.core.ui.theme.ACTION_BUTTON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.ANNOUNCEMENT_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.CARD_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.CATEGORY_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.DeepTeal
import eg.edu.cu.csds.icare.core.ui.theme.HEADER_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.HEADER_PROFILE_CARD_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.LightGreen
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Orange200
import eg.edu.cu.csds.icare.core.ui.theme.PROMOTION_BANNER_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.PROMOTION_ITEM_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.U_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.cardBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.kufamFamily
import eg.edu.cu.csds.icare.core.ui.theme.statusColor
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.data.util.getFormattedDate
import eg.edu.cu.csds.icare.data.util.getFormattedTime
import eg.edu.cu.csds.icare.home.R
import eg.edu.cu.csds.icare.home.component.PromotionItem
import eg.edu.cu.csds.icare.home.component.ServiceItem
import eg.edu.cu.csds.icare.home.component.TopDoctorCard
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun HomeContent(
    userResource: Resource<User>,
    topDoctorsRes: Resource<List<Doctor>>,
    appointmentsRes: Resource<List<Appointment>>,
    promotionsRes: Resource<List<Promotion>>,
    appVersion: String,
    statusList: List<AppointmentStatus>,
    onUserClicked: () -> Unit,
    onPromotionClicked: () -> Unit = {},
    onServiceClicked: (Screen) -> Unit = {},
    onDoctorClicked: (Doctor) -> Unit = {},
    onError: suspend (Throwable?) -> Unit,
    context: Context = LocalContext.current,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (progress, titleContainer, line, content, marquee) = createRefs()
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
            userResource.data?.let { user ->
                TitleView(
                    modifier = Modifier,
                    appVersion = appVersion,
                    user = user,
                    onUserClicked = { onUserClicked() },
                )
            }
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

        when (userResource) {
            is Resource.Unspecified -> {}
            is Resource.Loading ->
                CircularProgressIndicator(
                    modifier =
                        Modifier.constrainAs(progress) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                )

            is Resource.Success ->
                userResource.data?.let {
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
                                        text = stringResource(R.string.cosmetics),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        fontFamily = helveticaFamily,
                                        color = DeepTeal,
                                        maxLines = 1,
                                    )
                                    Text(
                                        text = " ${stringResource(R.string.discount_50)} ${
                                            stringResource(R.string.off)
                                        }",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                        fontFamily = helveticaFamily,
                                        color = Color.Gray,
                                        maxLines = 1,
                                    )
                                    Button(
                                        onClick = { onPromotionClicked() },
                                        modifier = Modifier.padding(top = S_PADDING),
                                        colors =
                                            ButtonDefaults.buttonColors(
                                                containerColor = DeepTeal,
                                            ),
                                    ) {
                                        Text(
                                            text = stringResource(R.string.buy_now),
                                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                            fontFamily = helveticaFamily,
                                            color = Color.White,
                                            maxLines = 1,
                                        )
                                    }
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.doctor_announcement),
                                    contentDescription = null,
                                    modifier = Modifier.size(ANNOUNCEMENT_IMAGE_SIZE),
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(M_PADDING))

                        Text(
                            text = stringResource(R.string.services),
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
                            ServiceItem(AppService.ScanCenter, onClick = { onServiceClicked(it) })
                            ServiceItem(
                                AppService.BookAppointment,
                                onClick = { onServiceClicked(it) },
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            ServiceItem(AppService.LabCenter, onClick = { onServiceClicked(it) })
                            ServiceItem(AppService.Pharmacy, onClick = { onServiceClicked(it) })
                        }

                        Spacer(modifier = Modifier.height(M_PADDING))

                        Text(
                            stringResource(R.string.next_appointment),
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
                                    .clickable {
                                        onServiceClicked(Screen.MyAppointments)
                                    },
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
                                val (doctorImage, doctorName, doctorSpecialty, status, dateIcon, date, timeIcon, time, message) =
                                    createRefs()
                                appointmentsRes.data?.let { appointments ->
                                    if (appointments.any {
                                            it.statusId == AppointmentStatus.PendingStatus.code ||
                                                it.statusId == AppointmentStatus.ConfirmedStatus.code
                                        }
                                    ) {
                                        val appointment =
                                            appointments.first().let { firstAppointment ->
                                                firstAppointment.copy(
                                                    status =
                                                        statusList
                                                            .find { it.code == firstAppointment.statusId }
                                                            ?.let {
                                                                stringResource(it.textResId)
                                                            }
                                                            ?: stringResource(CoreR.string.undefined),
                                                )
                                            }

                                        Box(
                                            modifier =
                                                Modifier
                                                    .constrainAs(doctorImage) {
                                                        top.linkTo(parent.top)
                                                        start.linkTo(parent.start)
                                                    }.size(ACTION_BUTTON_SIZE),
                                        ) {
                                            AsyncImage(
                                                model = appointment.doctorImage,
                                                contentDescription = null,
                                                placeholder = painterResource(R.drawable.user_placeholder),
                                                modifier =
                                                    Modifier
                                                        .clip(RoundedCornerShape(M_PADDING)),
                                                error = painterResource(R.drawable.user_placeholder),
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
                                                if (appointment.statusId == AppointmentStatus.ConfirmedStatus.code) {
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
                                            painterResource(R.drawable.baseline_access_time_24),
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
                                    } else {
                                        Text(
                                            text = stringResource(R.string.no_upcoming_appointments),
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
                        }

                        Spacer(modifier = Modifier.height(M_PADDING))

                        Text(
                            stringResource(R.string.promotions),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = helveticaFamily,
                            color = textColor,
                            maxLines = 1,
                        )
                        promotionsRes.data?.let { promotions ->
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                items(promotions, key = { promotion ->
                                    promotion.id
                                }) { promotion ->
                                    PromotionItem(promotion)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(M_PADDING))

                        Text(
                            stringResource(R.string.top_doctors),
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = helveticaFamily,
                            color = textColor,
                            maxLines = 1,
                        )
                        topDoctorsRes.data?.let { doctors ->
                            LazyRow {
                                items(doctors) { doctor ->
                                    TopDoctorCard(
                                        doctor,
                                        modifier =
                                            Modifier.clickable {
                                                onDoctorClicked(doctor)
                                            },
                                    )
                                }
                            }
                        }
                    }
                }

            is Resource.Error ->
                LaunchedEffect(key1 = true) {
                    onError(userResource.error)
                }
        }

        Text(
            text = stringResource(id = CoreR.string.made_by),
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

@Composable
private fun TitleView(
    appVersion: String,
    user: User,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onUserClicked: () -> Unit,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = S_PADDING, vertical = XS_PADDING),
    ) {
        val (logo, title, version, card) = createRefs()

        Image(
            modifier =
                Modifier
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }.size(HEADER_ICON_SIZE),
            painter = painterResource(CoreR.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Text(
            modifier =
                Modifier.constrainAs(title) {
                    start.linkTo(logo.end)
                    end.linkTo(card.end)
                    bottom.linkTo(logo.bottom)
                    width = Dimension.fillToConstraints
                },
            text = stringResource(CoreR.string.app_name),
            fontFamily = kufamFamily,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            color = textColor,
        )

        Surface(
            modifier =
                Modifier
                    .constrainAs(card) {
                        top.linkTo(logo.top, S_PADDING)
                        end.linkTo(parent.end, S_PADDING)
                    }.clickable { onUserClicked() },
            color = contentBackgroundColor,
            shape = RoundedCornerShape(S_PADDING),
        ) {
            ConstraintLayout(
                modifier =
                    Modifier
                        .width(HEADER_PROFILE_CARD_WIDTH)
                        .padding(U_PADDING),
            ) {
                val (image, name) = createRefs()

                Image(
                    modifier =
                        Modifier
                            .clip(CircleShape)
                            .size(CATEGORY_ICON_SIZE)
                            .constrainAs(image) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            },
                    painter =
                        rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(context)
                                .data(data = user.photoUrl)
                                .placeholder(R.drawable.user_placeholder)
                                .error(R.drawable.user_placeholder)
                                .build(),
                        ),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )

                Text(
                    modifier =
                        Modifier.constrainAs(name) {
                            top.linkTo(image.top)
                            start.linkTo(image.end, margin = XS_PADDING)
                            bottom.linkTo(image.bottom)
                        },
                    text = user.displayName,
                    color = contentColor,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFamily,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Text(
            text = "V. $appVersion",
            modifier =
                Modifier
                    .constrainAs(version) {
                        top.linkTo(title.top)
                        end.linkTo(parent.end, S_PADDING)
                    },
            color = Yellow700,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = helveticaFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun HomeContentPreview() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(XS_PADDING),
    ) {
        HomeContent(
            userResource =
                Resource.Success(
                    User(
                        roleId = Role.AdminRole.code,
                        displayName = "Ali Mansour",
                        email = "",
                        photoUrl = "",
                    ),
                ),
            topDoctorsRes =
                Resource.Success(
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
                            name = "Dr. Tiyah",
                            specialty = "General Practitioner",
                            rating = 4.5,
                        ),
                    ),
                ),
            appointmentsRes =
                Resource.Success(
                    listOf(
                        Appointment(
                            appointmentId = 1,
                            doctorName = "Dr. John Smith",
                            doctorSpecialty = "Cardiologist",
                            doctorImage = "https://t4.ftcdn.net/jpg/01/98/82/75/360_F_198827520_wVNNHdMq4yLJe76WWivQQ5Ev2WtXac4N.webp",
                            dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                            statusId = AppointmentStatus.ConfirmedStatus.code,
                        ),
                    ),
                ),
            promotionsRes =
                Resource.Success(
                    listOf(
                        Promotion(
                            id = 1,
                            imageUrl = "https://i.postimg.cc/5jjyk7Jn/promo1.png",
                            discount = stringResource(R.string.discount_30),
                        ),
                        Promotion(
                            id = 2,
                            imageUrl = "https://i.postimg.cc/vDjTRrHM/promo2.png",
                            discount = stringResource(R.string.discount_50),
                        ),
                    ),
                ),
            appVersion = "1.0.0",
            onUserClicked = {},
            onPromotionClicked = {},
            onServiceClicked = { },
            onDoctorClicked = {},
            onError = {},
            statusList =
                listOf(
                    AppointmentStatus.PendingStatus,
                    AppointmentStatus.ConfirmedStatus,
                    AppointmentStatus.CancelledStatus,
                    AppointmentStatus.CompletedStatus,
                ),
        )
    }
}
