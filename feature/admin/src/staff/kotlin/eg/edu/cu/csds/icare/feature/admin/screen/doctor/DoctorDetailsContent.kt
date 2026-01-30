package eg.edu.cu.csds.icare.feature.admin.screen.doctor

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import eg.edu.cu.csds.icare.core.data.util.getFormattedTime
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.M_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.DoubleTextField
import eg.edu.cu.csds.icare.feature.admin.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DoctorDetailsContent(
    uiState: DoctorState,
    onIntent: (DoctorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = M_PADDING)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = uiState.firstName,
            onValueChange = { onIntent(DoctorIntent.UpdateFirstName(it)) },
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_first_name),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f)
                    .padding(top = L_PADDING),
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
        )

        TextField(
            value = uiState.lastName,
            onValueChange = { onIntent(DoctorIntent.UpdateLastName(it)) },
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_last_name),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f),
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
        )

        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
            expanded = uiState.isClinicsExpanded,
            onExpandedChange = { onIntent(DoctorIntent.UpdateClinicsExpanded(it)) },
        ) {
            OutlinedTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                readOnly = true,
                value =
                    uiState.clinics.firstOrNull { it.id == uiState.clinicId }?.name ?: "",
                onValueChange = { },
                label = {
                    Text(
                        text = stringResource(R.string.feature_admin_clinic),
                        color = dropDownTextColor,
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = uiState.isClinicsExpanded,
                    )
                },
                colors =
                    ExposedDropdownMenuDefaults.textFieldColors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedTrailingIconColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                    ),
            )

            ExposedDropdownMenu(
                expanded = uiState.isClinicsExpanded,
                onDismissRequest = {
                    onIntent(DoctorIntent.UpdateClinicsExpanded(false))
                },
            ) {
                uiState.clinics.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name,
                                color = dropDownTextColor,
                            )
                        },
                        onClick = {
                            onIntent(DoctorIntent.UpdateClinicId(it.id))
                            onIntent(DoctorIntent.UpdateClinicsExpanded(false))
                        },
                    )
                }
            }
        }

        TextField(
            value = uiState.email,
            onValueChange = { onIntent(DoctorIntent.UpdateEmail(it)) },
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_email),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f),
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
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
        )

        TextField(
            value = uiState.phone,
            onValueChange = { if (it.length < 14) onIntent(DoctorIntent.UpdatePhone(it)) },
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_phone_number),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f),
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
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                ),
        )

        TextField(
            value = uiState.speciality,
            onValueChange = { onIntent(DoctorIntent.UpdateSpeciality(it)) },
            label = {
                Text(
                    text = stringResource(string.core_ui_speciality),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.8f),
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
        )

        DoubleTextField(
            value = uiState.price,
            onValueChanged = { onIntent(DoctorIntent.UpdatePrice(it)) },
            label = stringResource(string.core_ui_price),
            textColor = textColor,
        )

        DoubleTextField(
            value = uiState.rating,
            onValueChanged = { onIntent(DoctorIntent.UpdateRating(it)) },
            label = stringResource(string.core_ui_rating),
            textColor = textColor,
            readOnly = uiState.ratingReadOnly,
            imeAction = ImeAction.Done,
        )

        TextField(
            value = uiState.fromTime.getFormattedTime(context),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_from_time),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
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
                    modifier =
                        Modifier.clickable {
                            val calendar = Calendar.getInstance()
                            val hour = calendar.get(Calendar.HOUR_OF_DAY)
                            val minute = calendar.get(Calendar.MINUTE)

                            TimePickerDialog(
                                context,
                                { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                                    val selectedCalendar = Calendar.getInstance()
                                    selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                                    selectedCalendar.set(Calendar.MINUTE, selectedMinute)
                                    selectedCalendar.set(Calendar.SECOND, 0)
                                    selectedCalendar.set(Calendar.MILLISECOND, 0)

                                    val timeInMillis = selectedCalendar.timeInMillis

                                    onIntent(DoctorIntent.UpdateFromTime(timeInMillis))
                                },
                                hour,
                                minute,
                                true,
                            ).show()
                        },
                    painter = painterResource(id = drawable.core_ui_baseline_calendar_month_24),
                    contentDescription = "",
                )
            },
        )

        TextField(
            value = uiState.toTime.getFormattedTime(context),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(R.string.feature_admin_to_time),
                    fontFamily = helveticaFamily,
                    color = textColor,
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
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
                    modifier =
                        Modifier.clickable {
                            val calendar = Calendar.getInstance()
                            val hour = calendar.get(Calendar.HOUR_OF_DAY)
                            val minute = calendar.get(Calendar.MINUTE)

                            TimePickerDialog(
                                context,
                                { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                                    val selectedCalendar = Calendar.getInstance()
                                    selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                                    selectedCalendar.set(Calendar.MINUTE, selectedMinute)
                                    selectedCalendar.set(Calendar.SECOND, 0)
                                    selectedCalendar.set(Calendar.MILLISECOND, 0)

                                    val timeInMillis = selectedCalendar.timeInMillis

                                    onIntent(DoctorIntent.UpdateToTime(timeInMillis))
                                },
                                hour,
                                minute,
                                true,
                            ).show()
                        },
                    painter =
                        painterResource(
                            id = drawable.core_ui_baseline_calendar_month_24,
                        ),
                    contentDescription = "",
                )
            },
        )

        Spacer(modifier = Modifier.height(L_PADDING))

        AnimatedButton(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.6f),
            text = stringResource(string.core_ui_proceed),
            color = buttonBackgroundColor,
            onClick = { onIntent(DoctorIntent.Proceed) },
        )
    }
}

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
internal fun DoctorDetailsContentPreview() {
    IcareTheme {
        Box(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(M_PADDING),
        ) {
            DoctorDetailsContent(
                uiState = DoctorState(),
                onIntent = {},
            )
        }
    }
}
