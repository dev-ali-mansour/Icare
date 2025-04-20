package eg.edu.cu.csds.icare.admin.screen.pharmacy.pharmacist

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
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
internal fun PharmacistDetailsContent(
    firstName: String,
    lastName: String,
    pharmacyId: Long,
    email: String,
    phone: String,
    profilePicture: String,
    pharmaciesResource: Resource<List<Pharmacy>>,
    actionResource: Resource<Nothing?>,
    pharmaciesExpanded: Boolean,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onPharmaciesExpandedChange: (Boolean) -> Unit,
    onPharmaciesDismissRequest: () -> Unit,
    onPharmacyClicked: (Long) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onProfilePictureChanged: (String) -> Unit,
    onProceedButtonClicked: () -> Unit,
    onSuccess: () -> Unit,
    onError: suspend (Throwable?) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier =
            modifier
                .fillMaxSize()
                .padding(bottom = XL_PADDING),
    ) {
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
            when (pharmaciesResource) {
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
                    pharmaciesResource.data?.let { pharmacies ->

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
                    LaunchedEffect(key1 = true) {
                        onError(pharmaciesResource.error)
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

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun PharmacistDetailsContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        PharmacistDetailsContent(
            firstName = "",
            lastName = "",
            pharmacyId = 0,
            email = "",
            phone = "",
            profilePicture = "",
            pharmaciesResource = Resource.Success(listOf(Pharmacy(name = "صيدلية العزبي"))),
            actionResource = Resource.Unspecified(),
            pharmaciesExpanded = false,
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onPharmaciesExpandedChange = {},
            onPharmaciesDismissRequest = {},
            onPharmacyClicked = {},
            onEmailChanged = {},
            onPhoneChanged = {},
            onProfilePictureChanged = {},
            onProceedButtonClicked = {},
            onSuccess = {},
            onError = {},
        )
    }
}
