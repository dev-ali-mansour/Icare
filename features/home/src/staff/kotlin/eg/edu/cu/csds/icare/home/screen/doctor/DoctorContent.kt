package eg.edu.cu.csds.icare.home.screen.doctor

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.util.getFormattedTime
import eg.edu.cu.csds.icare.core.domain.util.toFormattedString
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.theme.APPOINTMENTS_LIST_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PATIENTS_CARD_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.SERVICE_ICON_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.STAT_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.STAT_CARD_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.SkyAccent
import eg.edu.cu.csds.icare.core.ui.theme.U_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.mintAccent
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.theme.trustBlue
import eg.edu.cu.csds.icare.home.R
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
fun DoctorContent(
    modifier: Modifier = Modifier,
    schedule: DoctorSchedule,
    onAppointmentClick: (Appointment) -> Unit = {},
    onSeeAllClick: () -> Unit = {},
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(M_PADDING),
    ) {
        var (patientsCard, confirmedCard, priceCard, slotsCard, appointments, list, seeAll) = createRefs()

        TotalPatientsCard(
            totalPatients = schedule.totalPatients,
            modifier =
                Modifier.constrainAs(patientsCard) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        StatCard(
            title = stringResource(AppointmentStatus.ConfirmedStatus.textResId),
            value = schedule.confirmed.toString(),
            modifier =
                Modifier.constrainAs(confirmedCard) {
                    top.linkTo(patientsCard.bottom, margin = M_PADDING)
                    start.linkTo(parent.start)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.price),
            value = "${schedule.price.toFormattedString()} ${stringResource(CoreR.string.egp)}",
            modifier =
                Modifier.constrainAs(priceCard) {
                    top.linkTo(confirmedCard.top)
                    start.linkTo(confirmedCard.end, margin = XS_PADDING)
                    end.linkTo(slotsCard.start, margin = XS_PADDING)
                },
        )

        StatCard(
            title = stringResource(CoreR.string.available_slots),
            value = schedule.availableSlots.toString(),
            modifier =
                Modifier.constrainAs(slotsCard) {
                    top.linkTo(confirmedCard.top)
                    end.linkTo(parent.end)
                },
        )

        Text(
            text = stringResource(R.string.today_appointment),
            modifier =
                Modifier.constrainAs(appointments) {
                    top.linkTo(confirmedCard.bottom, margin = M_PADDING)
                    start.linkTo(confirmedCard.start)
                },
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            textAlign = TextAlign.Start,
            fontFamily = helveticaFamily,
            color = textColor,
            maxLines = 1,
        )
        Text(
            text = stringResource(CoreR.string.see_all),
            modifier =
                Modifier
                    .constrainAs(seeAll) {
                        top.linkTo(appointments.top)
                        end.linkTo(slotsCard.end)
                    }.clickable { onSeeAllClick() },
            color = trustBlue,
        )

        LazyColumn(
            modifier =
                Modifier
                    .constrainAs(list) {
                        top.linkTo(appointments.bottom, margin = S_PADDING)
                        start.linkTo(confirmedCard.start)
                        end.linkTo(seeAll.end)
                    }.height(APPOINTMENTS_LIST_HEIGHT),
        ) {
            items(schedule.appointments.take(n = 4)) { appointment ->
                AppointmentItem(
                    modifier = Modifier.clickable { onAppointmentClick(appointment) },
                    appointment = appointment,
                )
            }
        }
    }
}

@Composable
fun TotalPatientsCard(
    totalPatients: Long,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.width(PATIENTS_CARD_WIDTH),
        colors = CardDefaults.cardColors(containerColor = SkyAccent),
        elevation = CardDefaults.cardElevation(XS_PADDING),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(S_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(SERVICE_ICON_SIZE),
                tint = mintAccent,
            )
            Column(
                modifier = Modifier.padding(M_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.total_patients),
                    fontFamily = helveticaFamily,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                )
                Text(
                    text = "$totalPatients+",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = helveticaFamily,
                    color = mintAccent,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Text(
                    stringResource(R.string.till_today),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = helveticaFamily,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .width(STAT_CARD_WIDTH)
                .height(STAT_CARD_HEIGHT)
                .padding(XS_PADDING),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(XS_PADDING),
    ) {
        Column(
            modifier = Modifier.padding(vertical = M_PADDING, horizontal = U_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = helveticaFamily,
                maxLines = 2,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(L_PADDING))

            Text(
                text = value,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = SkyAccent,
            )
            Spacer(modifier = Modifier.height(L_PADDING))
        }
    }
}

@Composable
fun AppointmentItem(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    appointment: Appointment,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = S_PADDING),
    ) {
        val (image, name, time) = createRefs()
        Image(
            modifier =
                Modifier
                    .padding(XS_PADDING)
                    .clip(CircleShape)
                    .border(BOARDER_SIZE, Color.DarkGray, CircleShape)
                    .size(SERVICE_ICON_SIZE)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
            painter =
                rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(context)
                        .data(data = appointment.patientImage)
                        .placeholder(CoreR.drawable.user_placeholder)
                        .error(CoreR.drawable.user_placeholder)
                        .build(),
                ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Text(
            appointment.patientName,
            modifier.constrainAs(name) {
                top.linkTo(image.top)
                start.linkTo(image.end, margin = L_PADDING)
                end.linkTo(time.start, margin = L_PADDING)
                bottom.linkTo(image.bottom)
                width = Dimension.fillToConstraints
            },
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            textAlign = TextAlign.Start,
            fontFamily = helveticaFamily,
            color = trustBlue,
            maxLines = 1,
        )

        Box(
            modifier =
                Modifier
                    .constrainAs(time) {
                        top.linkTo(image.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(image.bottom)
                    }.background(
                        color = SkyAccent,
                        shape = RoundedCornerShape(XS_PADDING),
                    ).padding(U_PADDING),
        ) {
            Text(
                appointment.dateTime.getFormattedTime(),
                color = Color.White,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun DoctorContentPreview() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(XS_PADDING),
    ) {
        DoctorContent(
            schedule =
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
            onAppointmentClick = {},
            onSeeAllClick = {},
        )
    }
}
