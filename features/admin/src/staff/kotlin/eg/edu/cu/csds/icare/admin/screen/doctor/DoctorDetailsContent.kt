package eg.edu.cu.csds.icare.admin.screen.doctor

import android.app.TimePickerDialog
import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.core.data.util.getFormattedTime
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.DoubleTextField
import java.util.Calendar
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DoctorDetailsContent(
    state: DoctorState,
    onIntent: (DoctorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current

    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = XL_PADDING),
    ) {
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
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = state.firstName,
                    onValueChange = { onIntent(DoctorIntent.UpdateFirstName(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.first_name),
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
                    value = state.lastName,
                    onValueChange = { onIntent(DoctorIntent.UpdateLastName(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.last_name),
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
                    expanded = state.isClinicsExpanded,
                    onExpandedChange = { onIntent(DoctorIntent.UpdateClinicsExpanded(it)) },
                ) {
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        value =
                            state.clinics.firstOrNull { it.id == state.clinicId }?.name ?: "",
                        onValueChange = { },
                        label = {
                            Text(
                                text = stringResource(R.string.clinic),
                                color = dropDownTextColor,
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = state.isClinicsExpanded,
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
                        expanded = state.isClinicsExpanded,
                        onDismissRequest = {
                            onIntent(DoctorIntent.UpdateClinicsExpanded(false))
                        },
                    ) {
                        state.clinics.forEach {
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
                    value = state.email,
                    onValueChange = { onIntent(DoctorIntent.UpdateEmail(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.email),
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
                    value = state.phone,
                    onValueChange = { if (it.length < 14) onIntent(DoctorIntent.UpdatePhone(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.phone_number),
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
                    value = state.speciality,
                    onValueChange = { onIntent(DoctorIntent.UpdateSpeciality(it)) },
                    label = {
                        Text(
                            text = stringResource(CoreR.string.speciality),
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
                    value = state.price,
                    onValueChanged = { onIntent(DoctorIntent.UpdatePrice(it)) },
                    label = stringResource(CoreR.string.price),
                    textColor = textColor,
                )

                DoubleTextField(
                    value = state.rating,
                    onValueChanged = { onIntent(DoctorIntent.UpdateRating(it)) },
                    label = stringResource(CoreR.string.rating),
                    textColor = textColor,
                    readOnly = state.ratingReadOnly,
                    imeAction = ImeAction.Done,
                )

                TextField(
                    value = state.fromTime.getFormattedTime(context),
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(R.string.from_time),
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
                            painter = painterResource(id = CoreR.drawable.baseline_calendar_month_24),
                            contentDescription = "",
                        )
                    },
                )

                TextField(
                    value = state.toTime.getFormattedTime(context),
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(R.string.to_time),
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
                                    id = CoreR.drawable.baseline_calendar_month_24,
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
                    text = stringResource(CoreR.string.proceed),
                    color = buttonBackgroundColor,
                    onClick = { onIntent(DoctorIntent.Proceed) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun DoctorDetailsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        DoctorDetailsContent(
            state = DoctorState(),
            onIntent = {},
        )
    }
}
