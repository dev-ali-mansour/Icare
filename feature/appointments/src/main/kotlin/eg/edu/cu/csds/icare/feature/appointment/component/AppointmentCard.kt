package eg.edu.cu.csds.icare.feature.appointment.component

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eg.edu.cu.csds.icare.feature.appointment.R
import eg.edu.cu.csds.icare.core.data.util.getFormattedDateTime
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Orange200
import eg.edu.cu.csds.icare.core.ui.theme.PaidColor
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onReschedule: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    showActions: Boolean,
    showConfirm: Boolean = false,
    context: Context = LocalContext.current,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(M_PADDING)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(weight = 1f)) {
                    Text(
                        text = appointment.doctorName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = helveticaFamily,
                    )
                    Text(
                        text = appointment.doctorSpecialty,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = helveticaFamily,
                    )
                    Text(
                        text = appointment.patientName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = helveticaFamily,
                    )
                    Text(
                        text = appointment.status,
                        fontFamily = helveticaFamily,
                        color =
                            when (appointment.status) {
                                "Confirmed" -> PaidColor
                                "Pending" -> Orange200
                                else -> Color.Gray
                            },
                    )
                }
            }

            Spacer(modifier = Modifier.height(S_PADDING))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = appointment.dateTime.getFormattedDateTime(context),
                    fontFamily = helveticaFamily,
                    modifier = Modifier.weight(weight = 1f),
                )

                if (showActions) {
                    Row {
                        if (showConfirm) {
                            TextButton(onClick = { onConfirm() }) {
                                Text(
                                    text = stringResource(R.string.feature_appointments_confirm),
                                    fontFamily = helveticaFamily,
                                )
                            }
                        } else {
                            TextButton(onClick = { onReschedule() }) {
                                Text(
                                    text =
                                        stringResource(
                                            string.core_ui_reschedule,
                                        ),
                                    fontFamily = helveticaFamily,
                                )
                            }
                            Spacer(modifier = Modifier.width(S_PADDING))
                            TextButton(onClick = { onCancel() }) {
                                Text(
                                    text = stringResource(android.R.string.cancel),
                                    fontFamily = helveticaFamily,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(locale = "ar", showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(locale = "ar", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppointmentCardPreview() {
    Column(
        modifier =
            Modifier
                .padding(XS_PADDING)
                .background(color = backgroundColor),
    ) {
        AppointmentCard(
            appointment =
                Appointment(
                    appointmentId = 1,
                    doctorName = "Dr. John Smith",
                    doctorSpecialty = "Cardiologist",
                    doctorId = "101",
                    doctorImage = "",
                    dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                    patientName = "Patient",
                    patientImage = "",
                    statusId = AppointmentStatus.ConfirmedStatus.code,
                    status = stringResource(AppointmentStatus.ConfirmedStatus.textResId),
                ),
            showActions = true,
            showConfirm = true,
            onReschedule = {},
            onCancel = {},
            onConfirm = {},
        )
    }
}
