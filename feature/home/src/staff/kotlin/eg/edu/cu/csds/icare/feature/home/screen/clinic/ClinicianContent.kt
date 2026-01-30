package eg.edu.cu.csds.icare.feature.home.screen.clinic

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.data.util.getFormattedDateTime
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.TAB_INDICATOR_ROUND_CORNER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.TabRowContainerColor
import eg.edu.cu.csds.icare.core.ui.theme.Yellow700
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.calculateGridColumns
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.EmptyContentView
import eg.edu.cu.csds.icare.feature.appointment.R
import eg.edu.cu.csds.icare.feature.appointment.component.AppointmentCard
import eg.edu.cu.csds.icare.feature.home.util.statusList

@Composable
fun ClinicianContent(
    appointments: List<Appointment>,
    columnsCount: Int,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    onConfirm: (Appointment) -> Unit,
) {
    val context: Context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val status = statusList[selectedTabIndex]
    val filteredAppointments =
        appointments.filter { it.statusId == status.code }.map { appointment ->
            statusList.find { it.code == appointment.statusId }?.let {
                appointment.copy(status = stringResource(it.textResId))
            } ?: appointment
        }
    val showActions = status.code == AppointmentStatus.PendingStatus.code
    Column(modifier = modifier.fillMaxSize()) {
        SecondaryScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = TabRowContainerColor,
            contentColor = Yellow700,
            edgePadding = M_PADDING,
            indicator = {
                Box(
                    modifier =
                        Modifier
                            .height(TAB_INDICATOR_HEIGHT)
                            .clip(
                                RoundedCornerShape(
                                    TAB_INDICATOR_ROUND_CORNER_SIZE,
                                ),
                            ).background(
                                color = MaterialTheme.colorScheme.primary,
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
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = helveticaFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.primary,
                )
            }
        }

        if (appointments.isEmpty()) {
            val message =
                "${
                    stringResource(
                        R.string.feature_appointments_no_appointments_data,
                    )
                }: ${stringResource(status.textResId)}"
            EmptyContentView(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(M_PADDING)
                        .verticalScroll(rememberScrollState()),
                text = message,
            )
            return@Column
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnsCount),
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            contentPadding = PaddingValues(all = S_PADDING),
            horizontalArrangement = Arrangement.spacedBy(S_PADDING),
            verticalArrangement = Arrangement.spacedBy(S_PADDING),
        ) {
            items(
                items = filteredAppointments,
                key = { appointment ->
                    appointment.id
                },
                span = { GridItemSpan(1) },
            ) { appointment ->
                AppointmentCard(
                    modifier = Modifier.padding(bottom = S_PADDING),
                    doctorName = appointment.doctorName,
                    doctorSpecialty = appointment.doctorSpecialty,
                    dateTime = appointment.dateTime.getFormattedDateTime(context),
                    patientName = appointment.patientName,
                    statusId = appointment.statusId,
                    status = appointment.status,
                    onReschedule = { },
                    onCancel = { },
                    onConfirm = { onConfirm(appointment) },
                    showActions = showActions,
                    showConfirm = true,
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
internal fun ClinicianContentPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(M_PADDING),
        ) {
            ClinicianContent(
                appointments =
                    listOf(
                        Appointment(
                            id = 1,
                            doctorName = "Dr. John Smith",
                            doctorSpecialty = "Cardiologist",
                            dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                            patientName = "Patient",
                            statusId = AppointmentStatus.PendingStatus.code,
                            status = stringResource(AppointmentStatus.PendingStatus.textResId),
                        ),
                        Appointment(
                            id = 2,
                            doctorName = "Dr. Sarah Johnson",
                            doctorSpecialty = "Dermatologist",
                            dateTime = System.currentTimeMillis() + Constants.TWO_DAYS,
                            patientName = "Patient",
                            statusId = AppointmentStatus.PendingStatus.code,
                            status = stringResource(AppointmentStatus.PendingStatus.textResId),
                        ),
                        Appointment(
                            id = 3,
                            doctorName = "Dr. Anna Jones",
                            doctorSpecialty = "General Practitioner",
                            dateTime = System.currentTimeMillis() + Constants.THREE_DAYS,
                            patientName = "Patient",
                            statusId = AppointmentStatus.PendingStatus.code,
                            status = stringResource(AppointmentStatus.PendingStatus.textResId),
                        ),
                        Appointment(
                            id = 4,
                            doctorName = "Dr. Emanuel Johnson",
                            doctorSpecialty = "Dermatologist",
                            dateTime = System.currentTimeMillis() + Constants.FOUR_DAYS,
                            patientName = "Patient",
                            statusId = AppointmentStatus.PendingStatus.code,
                            status = stringResource(AppointmentStatus.PendingStatus.textResId),
                        ),
                        Appointment(
                            id = 5,
                            doctorName = "Dr. Anna Jones",
                            doctorSpecialty = "General Practitioner",
                            dateTime = System.currentTimeMillis() + Constants.FIVE_DAYS,
                            patientName = "Patient",
                            statusId = AppointmentStatus.PendingStatus.code,
                            status = stringResource(AppointmentStatus.PendingStatus.textResId),
                        ),
                    ),
                columnsCount = calculateGridColumns(),
                onConfirm = {},
            )
        }
    }
}
