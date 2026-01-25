package eg.edu.cu.csds.icare.feature.appointment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.theme.MAX_SURFACE_WIDTH
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Orange200
import eg.edu.cu.csds.icare.core.ui.theme.PaidColor
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.util.neumorphicUp
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.feature.appointment.R

@Composable
fun AppointmentCard(
    doctorName: String,
    doctorSpecialty: String,
    dateTime: String,
    patientName: String,
    statusId: Short,
    status: String,
    modifier: Modifier = Modifier,
    onReschedule: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    showActions: Boolean,
    showConfirm: Boolean = false,
) {
    Box(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(percent = 20),
                ).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthIn(max = MAX_SURFACE_WIDTH)
                .neumorphicUp(
                    shape = RoundedCornerShape(percent = 20),
                    shadowPadding = XS_PADDING,
                ),
    ) {
        Column(modifier = Modifier.padding(M_PADDING)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(weight = 1f)) {
                    Text(
                        text = doctorName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = helveticaFamily,
                    )
                    Text(
                        text = doctorSpecialty,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = helveticaFamily,
                    )
                    Text(
                        text = patientName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = helveticaFamily,
                    )
                    Text(
                        text = status,
                        fontFamily = helveticaFamily,
                        color =
                            when (statusId) {
                                AppointmentStatus.PendingStatus.code -> Orange200
                                AppointmentStatus.ConfirmedStatus.code -> PaidColor
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
                    text = dateTime,
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

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
fun AppointmentCardPreview() {
    Column(
        modifier =
            Modifier
                .padding(XS_PADDING)
                .background(color = backgroundColor),
    ) {
        AppointmentCard(
            doctorName = "Dr. John Smith",
            doctorSpecialty = "Cardiologist",
            dateTime = "18/01/2026 01:30 PM",
            patientName = "Sara Watson",
            statusId = AppointmentStatus.ConfirmedStatus.code,
            status = stringResource(AppointmentStatus.ConfirmedStatus.textResId),
            showActions = true,
            showConfirm = true,
            onReschedule = {},
            onCancel = {},
            onConfirm = {},
        )

        Spacer(modifier = Modifier.height(S_PADDING))

        AppointmentCard(
            doctorName = "Dr. Alfred Hopkins",
            doctorSpecialty = "Therapist",
            dateTime = "23/01/2026 04:00 PM",
            patientName = "Emmy Jackson",
            statusId = AppointmentStatus.ConfirmedStatus.code,
            status = stringResource(AppointmentStatus.PendingStatus.textResId),
            showActions = true,
            showConfirm = true,
            onReschedule = {},
            onCancel = {},
            onConfirm = {},
        )
    }
}
