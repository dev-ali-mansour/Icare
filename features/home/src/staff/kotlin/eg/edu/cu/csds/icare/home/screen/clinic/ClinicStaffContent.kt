package eg.edu.cu.csds.icare.home.screen.clinic

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
import eg.edu.cu.csds.icare.appointment.components.AppointmentCard
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun ClinicStaffContent(
    appointmentsRes: Resource<List<Appointment>>,
    actionResource: Resource<Unit>,
    showLoading: (Boolean) -> Unit,
    onConfirm: (Appointment) -> Unit,
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
            when (appointmentsRes) {
                is Resource.Unspecified ->
                    LaunchedEffect(key1 = appointmentsRes) {
                        showLoading(
                            false,
                        )
                    }

                is Resource.Loading -> LaunchedEffect(key1 = appointmentsRes) { showLoading(true) }

                is Resource.Success -> {
                    LaunchedEffect(key1 = appointmentsRes) { showLoading(false) }
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
                                        onConfirm = { onConfirm(it) },
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
                    LaunchedEffect(key1 = appointmentsRes) {
                        showLoading(false)
                        onError(appointmentsRes.error)
                    }
            }
        }
        when (actionResource) {
            is Resource.Unspecified -> LaunchedEffect(key1 = actionResource) { showLoading(false) }
            is Resource.Loading -> LaunchedEffect(key1 = actionResource) { showLoading(true) }

            is Resource.Success ->
                LaunchedEffect(key1 = Unit) {
                    showLoading(false)
                    onSuccess()
                }

            is Resource.Error ->
                LaunchedEffect(key1 = actionResource) {
                    showLoading(false)
                    onError(actionResource.error)
                }
        }
    }
}

@Composable
fun UpcomingAppointmentsContent(
    appointments: List<Appointment>,
    onConfirm: (Appointment) -> Unit,
) {
    val pendingAppointments =
        appointments.filter { it.statusId == AppointmentStatus.PendingStatus.code }

    LazyColumn(
        modifier = Modifier.padding(M_PADDING),
        verticalArrangement = Arrangement.spacedBy(S_PADDING),
    ) {
        if (pendingAppointments.isNotEmpty()) {
            items(pendingAppointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onReschedule = { },
                    onCancel = { },
                    onConfirm = { onConfirm(appointment) },
                    showActions = true,
                    showConfirm = true,
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
                onConfirm = {},
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
internal fun ClinicStaffContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        ClinicStaffContent(
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
            showLoading = {},
            onConfirm = {},
            onSuccess = {},
            onError = {},
        )
    }
}
