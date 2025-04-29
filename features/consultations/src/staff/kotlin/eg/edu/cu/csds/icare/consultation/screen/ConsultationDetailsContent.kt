package eg.edu.cu.csds.icare.consultation.screen

import android.content.Context
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.runtime.mutableLongStateOf
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
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
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
import eg.edu.cu.csds.icare.data.util.getFormattedDate
import eg.edu.cu.csds.icare.data.util.getFormattedDateTime
import kotlinx.coroutines.launch
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConsultationDetailsContent(
    pharmaciesRes: Resource<List<Pharmacy>>,
    centersRes: Resource<List<LabImagingCenter>>,
    actionResource: Resource<Nothing?>,
    appointment: Appointment,
    readOnly: Boolean,
    dateTime: Long,
    diagnosis: String,
    pharmacyId: Long,
    pharmaciesExpanded: Boolean,
    medications: String,
    prescriptionStatusId: Short,
    labCenterId: Long,
    labCentersExpanded: Boolean,
    labTests: String,
    labTestStatusId: Short,
    imagingCenterId: Long,
    imagingCentersExpanded: Boolean,
    imagingTests: String,
    imgTestStatusId: Short,
    followUpdDate: Long,
    showLoading: (Boolean) -> Unit,
    onPatientCardClick: (String) -> Unit,
    onDiagnosisChanged: (String) -> Unit,
    onPharmaciesExpandedChange: (Boolean) -> Unit,
    onPharmaciesDismissRequest: () -> Unit,
    onPharmacyClicked: (Long) -> Unit,
    onMedicationsChanged: (String) -> Unit,
    onLabCentersExpandedChange: (Boolean) -> Unit,
    onLabCentersDismissRequest: () -> Unit,
    onLabCenterClicked: (Long) -> Unit,
    onLabTestsChanged: (String) -> Unit,
    onImagingCentersExpandedChange: (Boolean) -> Unit,
    onImagingCentersDismissRequest: () -> Unit,
    onImagingCenterClicked: (Long) -> Unit,
    onImagingTestsChanged: (String) -> Unit,
    onFollowUpDateChanged: (Long) -> Unit,
    onProceedButtonClicked: () -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    var selectedDate by remember { mutableLongStateOf(followUpdDate) }
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
                            selectedDate = it
                            selectedDateFormatted = it.getFormattedDate(context)
                            onFollowUpDateChanged(it)
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
                            .clickable { onPatientCardClick(appointment.patientId) },
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
                                        .data(data = appointment.patientImage)
                                        .placeholder(CoreR.drawable.user_placeholder)
                                        .error(CoreR.drawable.user_placeholder)
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
                                        .data(data = appointment.patientImage)
                                        .placeholder(CoreR.drawable.user_placeholder)
                                        .error(CoreR.drawable.user_placeholder)
                                        .build(),
                                ),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                        )

                        Text(
                            text = appointment.patientName,
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
                            text = stringResource(CoreR.string.view_medical_record),
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

                pharmaciesRes.data?.let { pharmacies ->
                    centersRes.data?.let { centers ->
                        val labCenters = centers.filter { it.type == 1.toShort() }
                        val imagingCenters = centers.filter { it.type == 2.toShort() }

                        Text(
                            text = "${stringResource(R.string.consultation_date)}: ${
                                dateTime.getFormattedDateTime(
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
                            value = diagnosis,
                            onValueChange = { onDiagnosisChanged(it) },
                            label = {
                                Text(
                                    text = stringResource(R.string.diagnosis),
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
                            expanded = pharmaciesExpanded,
                            onExpandedChange = {
                                onPharmaciesExpandedChange(it)
                            },
                        ) {
                            OutlinedTextField(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                readOnly = true,
                                value =
                                    pharmacies.firstOrNull { it.id == pharmacyId }?.name ?: "",
                                onValueChange = { },
                                label = {
                                    Text(
                                        text = stringResource(R.string.pharmacy),
                                        color = dropDownTextColor,
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = pharmaciesExpanded,
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
                                expanded = pharmaciesExpanded,
                                onDismissRequest = {
                                    onPharmaciesDismissRequest()
                                },
                            ) {
                                pharmacies.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = it.name,
                                                color = dropDownTextColor,
                                            )
                                        },
                                        onClick = { onPharmacyClicked(it.id) },
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = medications,
                            onValueChange = { onMedicationsChanged(it) },
                            label = {
                                Text(
                                    text = stringResource(R.string.Prescription),
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
                                statusList.firstOrNull { it.code == prescriptionStatusId }?.textResId
                                    ?: CoreR.string.pending,
                            )
                        Text(
                            text = "${stringResource(R.string.prescription_status)}: $prescriptionStatus",
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
                            expanded = labCentersExpanded,
                            onExpandedChange = {
                                onLabCentersExpandedChange(it)
                            },
                        ) {
                            OutlinedTextField(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                readOnly = true,
                                value =
                                    labCenters.firstOrNull { it.id == labCenterId }?.name ?: "",
                                onValueChange = { },
                                label = {
                                    Text(
                                        text = stringResource(R.string.lab_center),
                                        color = dropDownTextColor,
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = labCentersExpanded,
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
                                expanded = labCentersExpanded,
                                onDismissRequest = {
                                    onLabCentersDismissRequest()
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
                                        onClick = { onLabCenterClicked(it.id) },
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = labTests,
                            onValueChange = { onLabTestsChanged(it) },
                            label = {
                                Text(
                                    text = stringResource(R.string.lab_tests),
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
                                statusList.firstOrNull { it.code == labTestStatusId }?.textResId
                                    ?: CoreR.string.pending,
                            )
                        Text(
                            text = "${stringResource(R.string.lab_tests_status)}: $labTestsStatus",
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
                            expanded = imagingCentersExpanded,
                            onExpandedChange = {
                                onImagingCentersExpandedChange(it)
                            },
                        ) {
                            OutlinedTextField(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                readOnly = true,
                                value =
                                    imagingCenters.firstOrNull { it.id == imagingCenterId }?.name
                                        ?: "",
                                onValueChange = { },
                                label = {
                                    Text(
                                        text = stringResource(CoreR.string.imaging_center),
                                        color = dropDownTextColor,
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = imagingCentersExpanded,
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
                                expanded = imagingCentersExpanded,
                                onDismissRequest = {
                                    onImagingCentersDismissRequest()
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
                                        onClick = { onImagingCenterClicked(it.id) },
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = imagingTests,
                            onValueChange = { onImagingTestsChanged(it) },
                            label = {
                                Text(
                                    text = stringResource(R.string.imaging_tests),
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

                        val imagingTestsStatus =
                            stringResource(
                                statusList.firstOrNull { it.code == imgTestStatusId }?.textResId
                                    ?: CoreR.string.pending,
                            )
                        Text(
                            text = "${stringResource(R.string.imaging_tests_status)}: $imagingTestsStatus",
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
                                    text = stringResource(R.string.follow_up_date),
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
                                    imeAction = ImeAction.Next,
                                ),
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.clickable { openDatePicker() },
                                    painter = painterResource(id = CoreR.drawable.baseline_calendar_month_24),
                                    contentDescription = "",
                                )
                            },
                            interactionSource = interactionSource,
                        )

                        Spacer(modifier = Modifier.height(L_PADDING))

                        if (!readOnly) {
                            AnimatedButton(
                                modifier =
                                    Modifier
                                        .fillMaxWidth(fraction = 0.6f),
                                text = stringResource(CoreR.string.proceed),
                                color = buttonBackgroundColor,
                                onClick = { onProceedButtonClicked() },
                            )
                        }
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
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun ConsultationDetailsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        ConsultationDetailsContent(
            readOnly = false,
            pharmaciesRes = Resource.Success(listOf(Pharmacy(id = 1, name = "صيدلية الحياة"))),
            centersRes =
                Resource.Success(
                    listOf(
                        LabImagingCenter(id = 1, name = "معمل المختبر", type = 1),
                        LabImagingCenter(id = 2, name = "ألفا سكان", type = 2),
                    ),
                ),
            actionResource = Resource.Success(null),
            appointment =
                Appointment(
                    id = 1,
                    patientName = "سالم محمد السعيد",
                    patientImage = "https://t.pimg.jp/047/354/467/1/47354467.jpg",
                ),
            dateTime = System.currentTimeMillis(),
            diagnosis = "",
            pharmaciesExpanded = false,
            pharmacyId = 1,
            medications = "",
            prescriptionStatusId = 0,
            labCentersExpanded = false,
            labCenterId = 1,
            labTests = "",
            labTestStatusId = 0,
            imagingCentersExpanded = false,
            imagingCenterId = 2,
            imagingTests = "",
            imgTestStatusId = 0,
            followUpdDate = System.currentTimeMillis().plus(other = 7 * 24 * 60 * 60 * 1000),
            showLoading = {},
            onPatientCardClick = {},
            onDiagnosisChanged = {},
            onPharmacyClicked = {},
            onLabCenterClicked = {},
            onImagingCenterClicked = {},
            onMedicationsChanged = {},
            onLabTestsChanged = {},
            onImagingTestsChanged = {},
            onFollowUpDateChanged = {},
            onProceedButtonClicked = {},
            onSuccess = {},
            onError = {},
            onPharmaciesExpandedChange = {},
            onPharmaciesDismissRequest = { },
            onLabCentersExpandedChange = { },
            onLabCentersDismissRequest = {},
            onImagingCentersExpandedChange = { },
            onImagingCentersDismissRequest = { },
        )
    }
}
