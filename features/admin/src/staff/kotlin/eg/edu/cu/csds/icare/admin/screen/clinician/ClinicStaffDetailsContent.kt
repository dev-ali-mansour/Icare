package eg.edu.cu.csds.icare.admin.screen.clinician

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Resource
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
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ClinicStaffDetailsContent(
    firstName: String,
    lastName: String,
    clinicId: Long,
    email: String,
    phone: String,
    clinicsResource: Resource<List<Clinic>>,
    actionResource: Resource<Unit>,
    showLoading: (Boolean) -> Unit,
    clinicsExpanded: Boolean,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onClinicsExpandedChange: (Boolean) -> Unit,
    onClinicsDismissRequest: () -> Unit,
    onClinicClicked: (Long) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onProceedButtonClicked: () -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = XL_PADDING),
    ) {
        when (clinicsResource) {
            is Resource.Unspecified -> LaunchedEffect(key1 = clinicsResource) { showLoading(false) }
            is Resource.Loading -> LaunchedEffect(key1 = clinicsResource) { showLoading(true) }

            is Resource.Success -> {
                LaunchedEffect(key1 = clinicsResource) { showLoading(false) }
                clinicsResource.data?.let { clinics ->

                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(backgroundColor)
                                .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextField(
                            value = firstName,
                            onValueChange = { onFirstNameChanged(it) },
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
                            value = lastName,
                            onValueChange = { onLastNameChanged(it) },
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
                            expanded = clinicsExpanded,
                            onExpandedChange = {
                                onClinicsExpandedChange(it)
                            },
                        ) {
                            OutlinedTextField(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                readOnly = true,
                                value =
                                    clinics.firstOrNull { it.id == clinicId }?.name ?: "",
                                onValueChange = { },
                                label = {
                                    Text(
                                        text = stringResource(R.string.clinic),
                                        color = dropDownTextColor,
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = clinicsExpanded,
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
                                expanded = clinicsExpanded,
                                onDismissRequest = {
                                    onClinicsDismissRequest()
                                },
                            ) {
                                clinics.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = it.name,
                                                color = dropDownTextColor,
                                            )
                                        },
                                        onClick = { onClinicClicked(it.id) },
                                    )
                                }
                            }
                        }

                        TextField(
                            value = email,
                            onValueChange = { onEmailChanged(it) },
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
                            value = phone,
                            onValueChange = { if (it.length < 14) onPhoneChanged(it) },
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
                                    imeAction = ImeAction.Done,
                                ),
                        )

                        Spacer(modifier = Modifier.height(L_PADDING))

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

            is Resource.Error ->
                LaunchedEffect(key1 = clinicsResource) {
                    showLoading(false)
                    onError(clinicsResource.error)
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

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun ClinicStaffDetailsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        ClinicStaffDetailsContent(
            firstName = "",
            lastName = "",
            clinicId = 0,
            email = "",
            phone = "",
            clinicsResource = Resource.Success(listOf(Clinic(name = "عيادة 1"))),
            actionResource = Resource.Unspecified(),
            clinicsExpanded = false,
            showLoading = {},
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onClinicsExpandedChange = {},
            onClinicsDismissRequest = {},
            onClinicClicked = {},
            onEmailChanged = {},
            onPhoneChanged = {},
            onProceedButtonClicked = {},
            onSuccess = {},
            onError = {},
        )
    }
}
