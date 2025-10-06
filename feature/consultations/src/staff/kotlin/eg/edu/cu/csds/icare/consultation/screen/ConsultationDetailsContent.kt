package eg.edu.cu.csds.icare.consultation.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import eg.edu.cu.csds.icare.consultation.R
import eg.edu.cu.csds.icare.core.data.util.getFormattedDate
import eg.edu.cu.csds.icare.core.data.util.getFormattedDateTime
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import eg.edu.cu.csds.icare.core.ui.theme.BOARDER_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.CONSULTATION_PATIENT_CARD_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.PROFILE_IMAGE_SIZE
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.SkyAccent
import eg.edu.cu.csds.icare.core.ui.theme.TEXT_AREA_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.XS_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.feature.admin.R.string.feature_admin_pharmacy
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConsultationDetailsContent(
    uiState: ConsultationState,
    modifier: Modifier = Modifier,
    onEvent: (ConsultationEvent) -> Unit,
) {
    val context = LocalContext.current
    var selectedDateFormatted by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    var showDatePicker by remember { mutableStateOf(false) }
    val openDatePicker = { showDatePicker = true }

    val statusList =
        remember { listOf(AppointmentStatus.PendingStatus, AppointmentStatus.ConfirmedStatus) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDateFormatted = it.getFormattedDate(context)
                            onEvent(ConsultationEvent.UpdateFollowUpdDate(it))
                        }
                        showDatePicker = false
                    },
                ) {
                    Text(text = stringResource(id = android.R.string.ok), color = dropDownTextColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(
                        text = stringResource(id = android.R.string.cancel),
                        color = dropDownTextColor,
                    )
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    LaunchedEffect(interactionSource) {
        scope.launch {
            interactionSource.interactions.collect {
                if (it is PressInteraction.Release) {
                    openDatePicker()
                }
            }
        }
    }

    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = M_PADDING),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (details) = createRefs()

            Column(
                modifier =
                    Modifier
                        .constrainAs(details) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(CONSULTATION_PATIENT_CARD_HEIGHT)
                            .background(backgroundColor)
                            .padding(XS_PADDING)
                            .clickable {
                                onEvent(
                                    ConsultationEvent
                                        .NavigateToMedicalRecord(uiState.appointment.patientId),
                                )
                            },
                    colors = CardDefaults.cardColors(containerColor = SkyAccent),
                    elevation = CardDefaults.cardElevation(XS_PADDING),
                ) {
                    ConstraintLayout(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(M_PADDING),
                    ) {
                        val (image, name, message) = createRefs()
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
                                    },
                            painter =
                                rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(context)
                                        .data(data = uiState.appointment.patientImage)
                                        .placeholder(drawable.core_ui_user_placeholder)
                                        .error(drawable.core_ui_user_placeholder)
                                        .build(),
                                ),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                        )

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
                                        .data(data = uiState.appointment.patientImage)
                                        .placeholder(drawable.core_ui_user_placeholder)
                                        .error(drawable.core_ui_user_placeholder)
                                        .build(),
                                ),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                        )

                        Text(
                            text = uiState.appointment.patientName,
                            modifier =
                                Modifier.constrainAs(name) {
                                    top.linkTo(image.bottom, margin = M_PADDING)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = Color.White,
                            maxLines = 1,
                        )

                        Text(
                            text = stringResource(string.core_ui_view_medical_record),
                            modifier =
                                Modifier.constrainAs(message) {
                                    top.linkTo(name.bottom, margin = S_PADDING)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            textAlign = TextAlign.Start,
                            fontFamily = helveticaFamily,
                            color = Color.White,
                            maxLines = 1,
                        )
                    }
                }
                val labCenters = uiState.centers.filter { it.type == 1.toShort() }
                val imagingCenters = uiState.centers.filter { it.type == 2.toShort() }

                Text(
                    text = "${stringResource(R.string.feature_consultations_consultation_date)}: ${
                        uiState.dateTime.getFormattedDateTime(
                            context,
                        )
                    }",
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )

                OutlinedTextField(
                    value = uiState.diagnosis,
                    onValueChange = { onEvent(ConsultationEvent.UpdateDiagnosis(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_consultations_diagnosis),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    maxLines = 5,
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f)
                            .height(TEXT_AREA_HEIGHT),
                    shape = RoundedCornerShape(S_PADDING),
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = contentColor,
                            focusedTextColor = textColor,
                            focusedIndicatorColor = Yellow500,
                            unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                        ),
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Default,
                        ),
                )

                ExposedDropdownMenuBox(
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f),
                    expanded = uiState.isPharmaciesExpanded,
                    onExpandedChange = { onEvent(ConsultationEvent.UpdatePharmaciesExpanded(it)) },
                ) {
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        value =
                            uiState.pharmacies
                                .firstOrNull {
                                    it.id == uiState.pharmacyId
                                }?.name ?: "",
                        onValueChange = { },
                        label = {
                            Text(
                                text = stringResource(feature_admin_pharmacy),
                                color = dropDownTextColor,
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = uiState.isPharmaciesExpanded,
                            )
                        },
                        colors =
                            ExposedDropdownMenuDefaults.textFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = barBackgroundColor,
                                unfocusedContainerColor = barBackgroundColor,
                                unfocusedTrailingIconColor = Color.White,
                                focusedTrailingIconColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                            ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isPharmaciesExpanded,
                        onDismissRequest = {
                            onEvent(ConsultationEvent.UpdatePharmaciesExpanded(false))
                        },
                    ) {
                        uiState.pharmacies.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.name,
                                        color = dropDownTextColor,
                                    )
                                },
                                onClick = {
                                    onEvent(ConsultationEvent.UpdatePharmacyId(it.id))
                                    onEvent(ConsultationEvent.UpdatePharmaciesExpanded(false))
                                },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.medications,
                    onValueChange = { onEvent(ConsultationEvent.UpdateMedications(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_consultations_Prescription),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    maxLines = 5,
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f)
                            .height(TEXT_AREA_HEIGHT),
                    shape = RoundedCornerShape(S_PADDING),
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = contentColor,
                            focusedTextColor = textColor,
                            focusedIndicatorColor = Yellow500,
                            unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                        ),
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Default,
                        ),
                )

                val prescriptionStatus =
                    stringResource(
                        statusList.firstOrNull { it.code == uiState.prescriptionStatusId }?.textResId
                            ?: eg.edu.cu.csds.icare.core.ui.R.string.core_ui_pending,
                    )
                Text(
                    text = "${
                        stringResource(
                            R.string.feature_consultations_prescription_status,
                        )
                    }: $prescriptionStatus",
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )

                ExposedDropdownMenuBox(
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f),
                    expanded = uiState.isLabCentersExpanded,
                    onExpandedChange = {
                        onEvent(ConsultationEvent.UpdateLabCentersExpanded(it))
                    },
                ) {
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        value =
                            labCenters.firstOrNull { it.id == uiState.labCenterId }?.name ?: "",
                        onValueChange = { },
                        label = {
                            Text(
                                text = stringResource(string.core_ui_lab_center),
                                color = dropDownTextColor,
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = uiState.isLabCentersExpanded,
                            )
                        },
                        colors =
                            ExposedDropdownMenuDefaults.textFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = barBackgroundColor,
                                unfocusedContainerColor = barBackgroundColor,
                                unfocusedTrailingIconColor = Color.White,
                                focusedTrailingIconColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                            ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isLabCentersExpanded,
                        onDismissRequest = {
                            onEvent(ConsultationEvent.UpdateLabCentersExpanded(false))
                        },
                    ) {
                        labCenters.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.name,
                                        color = dropDownTextColor,
                                    )
                                },
                                onClick = {
                                    onEvent(ConsultationEvent.UpdateLabCenterId(it.id))
                                    onEvent(ConsultationEvent.UpdateLabCentersExpanded(false))
                                },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.labTests,
                    onValueChange = { onEvent(ConsultationEvent.UpdateLabTests(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_consultations_lab_tests),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    maxLines = 5,
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f)
                            .height(TEXT_AREA_HEIGHT),
                    shape = RoundedCornerShape(S_PADDING),
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = contentColor,
                            focusedTextColor = textColor,
                            focusedIndicatorColor = Yellow500,
                            unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                        ),
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Default,
                        ),
                )

                val labTestsStatus =
                    stringResource(
                        statusList.firstOrNull { it.code == uiState.labTestStatusId }?.textResId
                            ?: string.core_ui_pending,
                    )
                Text(
                    text = "${
                        stringResource(
                            R.string.feature_consultations_lab_tests_status,
                        )
                    }: $labTestsStatus",
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )

                ExposedDropdownMenuBox(
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f),
                    expanded = uiState.isImagingCentersExpanded,
                    onExpandedChange = {
                        onEvent(ConsultationEvent.UpdateImagingCentersExpanded(it))
                    },
                ) {
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        value =
                            imagingCenters.firstOrNull { it.id == uiState.imagingCenterId }?.name
                                ?: "",
                        onValueChange = { },
                        label = {
                            Text(
                                text =
                                    stringResource(
                                        eg.edu.cu.csds.icare.core.ui.R.string.core_ui_imaging_center,
                                    ),
                                color = dropDownTextColor,
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = uiState.isImagingCentersExpanded,
                            )
                        },
                        colors =
                            ExposedDropdownMenuDefaults.textFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = barBackgroundColor,
                                unfocusedContainerColor = barBackgroundColor,
                                unfocusedTrailingIconColor = Color.White,
                                focusedTrailingIconColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                            ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isImagingCentersExpanded,
                        onDismissRequest = {
                            onEvent(ConsultationEvent.UpdateImagingCentersExpanded(false))
                        },
                    ) {
                        imagingCenters.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.name,
                                        color = dropDownTextColor,
                                    )
                                },
                                onClick = {
                                    onEvent(ConsultationEvent.UpdateImagingCenterId(it.id))
                                    onEvent(ConsultationEvent.UpdateImagingCentersExpanded(false))
                                },
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.imagingTests,
                    onValueChange = { onEvent(ConsultationEvent.UpdateImagingTests(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_consultations_imaging_tests),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    maxLines = 5,
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f)
                            .height(TEXT_AREA_HEIGHT),
                    shape = RoundedCornerShape(S_PADDING),
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = contentColor,
                            focusedTextColor = textColor,
                            focusedIndicatorColor = Yellow500,
                            unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                        ),
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                )

                val imagingTestsStatus =
                    stringResource(
                        statusList.firstOrNull { it.code == uiState.imgTestStatusId }?.textResId
                            ?: eg.edu.cu.csds.icare.core.ui.R.string.core_ui_pending,
                    )
                Text(
                    text = "${
                        stringResource(
                            R.string.feature_consultations_imaging_tests_status,
                        )
                    }: $imagingTestsStatus",
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )

                TextField(
                    value = selectedDateFormatted,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(R.string.feature_consultations_follow_up_date),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    singleLine = true,
                    modifier =
                        Modifier
                            .padding(top = L_PADDING)
                            .fillMaxWidth(fraction = 0.8f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                            ) {
                                openDatePicker()
                            },
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = contentColor,
                            focusedTextColor = textColor,
                            focusedIndicatorColor = Yellow500,
                            unfocusedIndicatorColor = Yellow500.copy(alpha = 0.38f),
                        ),
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable { openDatePicker() },
                            painter = painterResource(id = drawable.core_ui_baseline_calendar_month_24),
                            contentDescription = "",
                        )
                    },
                    interactionSource = interactionSource,
                )

                Spacer(modifier = Modifier.height(L_PADDING))

                if (!uiState.readOnly) {
                    AnimatedButton(
                        modifier =
                            Modifier
                                .fillMaxWidth(fraction = 0.6f),
                        text = stringResource(string.core_ui_proceed),
                        color = buttonBackgroundColor,
                        onClick = { onEvent(ConsultationEvent.Proceed) },
                    )
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
internal fun ConsultationDetailsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        ConsultationDetailsContent(
            uiState =
                ConsultationState(
                    pharmacies = listOf(Pharmacy(id = 1, name = "صيدلية الحياة")),
                    centers =
                        listOf(
                            LabImagingCenter(id = 1, name = "معمل المختبر", type = 1),
                            LabImagingCenter(id = 2, name = "ألفا سكان", type = 2),
                        ),
                    appointment =
                        Appointment(
                            id = 1,
                            patientName = "سالم محمد السعيد",
                            patientImage = "https://t.pimg.jp/047/354/467/1/47354467.jpg",
                        ),
                    pharmacyId = 1,
                    labCenterId = 1,
                    imagingCenterId = 2,
                    dateTime = System.currentTimeMillis(),
                    readOnly = false,
                ),
            onEvent = {},
        )
    }
}
