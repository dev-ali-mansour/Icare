package eg.edu.cu.csds.icare.appointment.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.appointment.components.AppointmentCard
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isTomorrow
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun MyAppointmentsContent(
    appointmentsRes: Resource<List<Appointment>>,
    actionResource: Resource<Nothing?>,
    onReschedule: (Appointment) -> Unit,
    onCancel: (Appointment) -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    modifier: Modifier = Modifier,
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
        val (card, loading) = createRefs()

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
            when (appointmentsRes) {
                is Resource.Unspecified -> {}
                is Resource.Loading ->
                    CircularProgressIndicator(
                        modifier =
                            Modifier
                                .padding(XL_PADDING)
                                .constrainAs(loading) {
                                    top.linkTo(parent.top, margin = L_PADDING)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                },
                    )

                is Resource.Success -> {
                    appointmentsRes.data?.let { list ->
                        val appointments =
                            list.map { appointment ->
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
                                        onReschedule = { onReschedule(it) },
                                        onCancel = { onCancel(it) },
                                    )

                                statusList.indexOf(AppointmentStatus.ConfirmedStatus),
                                statusList.indexOf(AppointmentStatus.CompletedStatus),
                                statusList.indexOf(AppointmentStatus.CancelledStatus),
                                ->
                                    OtherAppointmentsContent(
                                        appointments.filter {
                                            it.statusId == statusList[selectedTabIndex].code
                                        },
                                    )
                            }
                        }
                    }
                }

                is Resource.Error ->
                    LaunchedEffect(key1 = true) {
                        onError(appointmentsRes.error)
                    }
            }
        }

        when (actionResource) {
            is Resource.Unspecified -> {}
            is Resource.Loading ->
                CircularProgressIndicator(
                    modifier =
                        Modifier
                            .padding(XL_PADDING)
                            .constrainAs(loading) {
                                top.linkTo(parent.top, margin = L_PADDING)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            },
                )

            is Resource.Success ->
                LaunchedEffect(key1 = Unit) {
                    onSuccess()
                }

            is Resource.Error ->
                LaunchedEffect(key1 = true) {
                    onError(actionResource.error)
                }
        }
    }
}

@Composable
fun UpcomingAppointmentsContent(
    appointments: List<Appointment>,
    onReschedule: (Appointment) -> Unit,
    onCancel: (Appointment) -> Unit,
) {
    val tomorrowAppointments =
        appointments.filter {
            it.statusId == AppointmentStatus.PendingStatus.code &&
                it.dateTime.isTomorrow()
        }

    val pendingAppointments =
        appointments.filter { it.statusId == AppointmentStatus.PendingStatus.code && !it.dateTime.isTomorrow() }

    LazyColumn(
        modifier = Modifier.padding(M_PADDING),
        verticalArrangement = Arrangement.spacedBy(S_PADDING),
    ) {
        if (tomorrowAppointments.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.tomorrow),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = helveticaFamily,
                    modifier = Modifier.padding(vertical = S_PADDING),
                )
            }

            items(tomorrowAppointments) { appointment ->
                // Todo Show DateTime picker to reschedule the appointment
                AppointmentCard(
                    appointment = appointment,
                    onReschedule = { onReschedule(appointment) },
                    onCancel = { onCancel(appointment.copy(statusId = AppointmentStatus.CancelledStatus.code)) },
                    showActions = true,
                )
            }
        }

        if (pendingAppointments.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.pending_for_confirmation),
                    fontFamily = helveticaFamily,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }

            items(pendingAppointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onReschedule = { onReschedule(appointment) },
                    onCancel = { onCancel(appointment) },
                    showActions = true,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun OtherAppointmentsContent(appointments: List<Appointment>) {
    LazyColumn(
        modifier = Modifier.padding(M_PADDING),
        verticalArrangement = Arrangement.spacedBy(S_PADDING),
    ) {
        items(appointments) { appointment ->
            AppointmentCard(
                appointment = appointment,
                onReschedule = {},
                onCancel = {},
                showActions = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun MyAppointmentContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        MyAppointmentsContent(
            appointmentsRes =
                Resource.Success(
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
            actionResource = Resource.Unspecified(),
            onReschedule = {},
            onCancel = {},
            onSuccess = {},
            onError = {},
        )
    }
}
