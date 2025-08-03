package eg.edu.cu.csds.icare.auth.screen.signup

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.data.util.getFormattedDate
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.domain.util.isValidPassword
import eg.edu.cu.csds.icare.core.ui.common.GenderItem
import eg.edu.cu.csds.icare.core.ui.theme.Blue200
import eg.edu.cu.csds.icare.core.ui.theme.Blue500
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TEXT_AREA_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.XL4_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.barBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.getErrorMessage
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.core.ui.view.DialogWithIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@Composable
internal fun SignUpScreen(
    firebaseAuth: FirebaseAuth,
    authViewModel: AuthViewModel,
    onLoginClicked: () -> Unit,
    onRegisterCompleted: () -> Unit,
    context: Context = LocalContext.current,
) {
    var firstName by authViewModel.firstNameState
    var lastName by authViewModel.lastNameState
    var email by authViewModel.emailState
    var birthDate by authViewModel.birthDateState
    var phone by authViewModel.phoneState
    var nationalId by authViewModel.nationalIdState
    var address by authViewModel.addressState
    var password by authViewModel.passwordState
    var chronicDiseases by authViewModel.chronicDiseasesState
    var currentMedications by authViewModel.currentMedicationsState
    var allergies by authViewModel.allergiesState
    var pastSurgeries by authViewModel.pastSurgeriesState
    var weight by authViewModel.weightState
    var passwordVisibility by authViewModel.passwordVisibility
    val isLoading by authViewModel.isLoading
    val resource by authViewModel.registerResFlow.collectAsStateWithLifecycle()
    val selectedGender by authViewModel.selectedGenderState
    var gendersExpanded by authViewModel.gendersExpanded
    val scope: CoroutineScope = rememberCoroutineScope()
    var alertMessage by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        LaunchedEffect(key1 = resource) {
            if (resource is Resource.Success) {
                alertMessage = context.getString(R.string.register_message)
                showAlert = true
                delay(timeMillis = 3000)
                firebaseAuth.signOut()
                showAlert = false
                authViewModel.onLogOutClick()
                onRegisterCompleted()
            } else if (resource is Resource.Error) {
                scope.launch {
                    alertMessage = context.getErrorMessage(resource.error)
                    showAlert = true
                    delay(timeMillis = 3000)
                    showAlert = false
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .background(color = backgroundColor)
                    .fillMaxSize()
                    .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter,
        ) {
            SignUpContent(
                firstName = firstName,
                lastName = lastName,
                email = email,
                birthDate = birthDate,
                selectedGender = selectedGender,
                genderExpanded = gendersExpanded,
                nationalId = nationalId,
                phone = phone,
                address = address,
                password = password,
                chronicDiseases = chronicDiseases,
                currentMedications = currentMedications,
                allergies = allergies,
                pastSurgeries = pastSurgeries,
                weight = weight,
                passwordVisibility = passwordVisibility,
                isLoading = isLoading,
                onFirstNameChange = { firstName = it },
                onLastNameChanged = { lastName = it },
                onEmailChange = { email = it },
                onBirthDateChanged = { birthDate = it },
                onGendersExpandedChange = { gendersExpanded = !gendersExpanded },
                onGendersDismissRequest = { gendersExpanded = false },
                onGenderClicked = {
                    authViewModel.onGenderSelected(it)
                    gendersExpanded = false
                },
                onPhoneChange = { phone = it },
                onAddressChange = { address = it },
                onNationalIdChange = { nationalId = it },
                onPasswordChange = { password = it },
                onPasswordVisibilityChange = { passwordVisibility = !passwordVisibility },
                onRegisterButtonClicked = {
                    scope.launch {
                        when {
                            firstName.isBlank() -> {
                                alertMessage = context.getString(R.string.first_name_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            lastName.isBlank() -> {
                                alertMessage = context.getString(R.string.last_name_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            !email.isValidEmail -> {
                                alertMessage = context.getString(R.string.email_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            selectedGender == 0.toShort() -> {
                                alertMessage = context.getString(R.string.gender_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            nationalId.isBlank() || nationalId.length < Constants.NATIONAL_ID_LENGTH -> {
                                alertMessage = context.getString(CoreR.string.error_national_id)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            phone.isBlank() || phone.length < Constants.PHONE_LENGTH -> {
                                alertMessage = context.getString(R.string.phone_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            chronicDiseases.isBlank() -> {
                                alertMessage = context.getString(R.string.chronic_diseases_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            currentMedications.isBlank() -> {
                                alertMessage = context.getString(R.string.current_medications_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            allergies.isBlank() -> {
                                alertMessage = context.getString(R.string.allergies_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            pastSurgeries.isBlank() -> {
                                alertMessage = context.getString(R.string.past_surgeries_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            !password.isValidPassword -> {
                                alertMessage = context.getString(R.string.password_error)
                                showAlert = true
                                delay(timeMillis = 3000)
                                showAlert = false
                            }

                            else -> authViewModel.onRegisterClicked()
                        }
                    }
                },
                onLoginClicked = { onLoginClicked() },
                onChronicDiseasesChange = { chronicDiseases = it },
                onCurrentMedicationsChange = { currentMedications = it },
                onAllergiesChange = { allergies = it },
                onPastSurgeriesChange = { pastSurgeries = it },
                onWeightChange = { weight = it },
            )

            if (showAlert) DialogWithIcon(text = alertMessage) { showAlert = false }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUpContent(
    firstName: String,
    lastName: String,
    email: String,
    birthDate: Long,
    selectedGender: Short,
    genderExpanded: Boolean,
    nationalId: String,
    phone: String,
    address: String,
    password: String,
    chronicDiseases: String,
    currentMedications: String,
    allergies: String,
    pastSurgeries: String,
    weight: Double,
    passwordVisibility: Boolean,
    isLoading: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthDateChanged: (Long) -> Unit,
    onGendersExpandedChange: (Boolean) -> Unit,
    onGendersDismissRequest: () -> Unit,
    onGenderClicked: (Short) -> Unit,
    onNationalIdChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onChronicDiseasesChange: (String) -> Unit,
    onCurrentMedicationsChange: (String) -> Unit,
    onAllergiesChange: (String) -> Unit,
    onPastSurgeriesChange: (String) -> Unit,
    onWeightChange: (Double) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onRegisterButtonClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    context: Context = LocalContext.current,
) {
    val genders =
        listOf(
            GenderItem(code = 1, textResId = CoreR.string.male),
            GenderItem(code = 2, textResId = CoreR.string.female),
        )
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    var showDatePicker by remember { mutableStateOf(false) }
    val openDatePicker = { showDatePicker = true }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onBirthDateChanged(it)
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

    ConstraintLayout(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = XL_PADDING),
    ) {
        val (topBox, card, loading) = createRefs()
        Box(
            modifier =
                Modifier
                    .constrainAs(topBox) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }.fillMaxWidth()
                    .height(300.dp)
                    .background(
                        brush =
                            Brush.verticalGradient(
                                startY = 0.0f,
                                colors =
                                    listOf(
                                        Blue500,
                                        Blue200,
                                        Color.White,
                                    ),
                            ),
                    ),
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (txtLogin) = createRefs()
                Text(
                    text = stringResource(R.string.create_account),
                    fontSize = MaterialTheme.typography.displaySmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = helveticaFamily,
                    color = Color.White,
                    modifier =
                        Modifier.constrainAs(txtLogin) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom, margin = XL_PADDING)
                            start.linkTo(parent.start, S_PADDING)
                        },
                )
            }
        }
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .constrainAs(card) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 190.dp)
                        height = Dimension.fillToConstraints
                    }.clip(
                        RoundedCornerShape(
                            topStart = XL4_PADDING,
                            topEnd = XL4_PADDING,
                        ),
                    ),
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
                    value = firstName,
                    onValueChange = { onFirstNameChange(it) },
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

                TextField(
                    value = email,
                    onValueChange = { onEmailChange(it) },
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
                    value = birthDate.getFormattedDate(context),
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(CoreR.string.birth_date),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    singleLine = true,
                    modifier =
                        Modifier
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

                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    expanded = genderExpanded,
                    onExpandedChange = {
                        onGendersExpandedChange(it)
                    },
                ) {
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        value =
                            genders.firstOrNull { it.code == selectedGender }?.textResId?.let {
                                stringResource(it)
                            } ?: "",
                        onValueChange = { },
                        label = {
                            Text(
                                text = stringResource(CoreR.string.gender),
                                color = dropDownTextColor,
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = genderExpanded,
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
                        expanded = genderExpanded,
                        onDismissRequest = {
                            onGendersDismissRequest()
                        },
                    ) {
                        genders.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(it.textResId),
                                        color = dropDownTextColor,
                                    )
                                },
                                onClick = { onGenderClicked(it.code) },
                            )
                        }
                    }
                }

                TextField(
                    value = nationalId,
                    onValueChange = { if (it.length < 15) onNationalIdChange(it) },
                    label = {
                        Text(
                            text = stringResource(R.string.national_id),
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
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                )
                TextField(
                    value = phone,
                    onValueChange = { if (it.length < 14) onPhoneChange(it) },
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
                    value = address,
                    onValueChange = { onAddressChange(it) },
                    label = {
                        Text(
                            text = stringResource(R.string.address),
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

                TextField(
                    value = weight.toString(),
                    onValueChange = {
                        runCatching {
                            onWeightChange(if (it.isEmpty()) 0.0 else it.toDouble())
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(CoreR.string.weight),
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
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                )

                OutlinedTextField(
                    value = chronicDiseases,
                    onValueChange = { onChronicDiseasesChange(it) },
                    label = {
                        Text(
                            text = stringResource(CoreR.string.chronic_diseases),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.chronic_diseases_hint)) },
                    singleLine = false,
                    maxLines = 3,
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

                OutlinedTextField(
                    value = currentMedications,
                    onValueChange = { onCurrentMedicationsChange(it) },
                    label = {
                        Text(
                            text = stringResource(CoreR.string.current_medications),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.current_medications_hint)) },
                    singleLine = false,
                    maxLines = 3,
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

                OutlinedTextField(
                    value = allergies,
                    onValueChange = { onAllergiesChange(it) },
                    label = {
                        Text(
                            text = stringResource(CoreR.string.allergies),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.allergies_hint)) },
                    singleLine = false,
                    maxLines = 3,
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

                OutlinedTextField(
                    value = pastSurgeries,
                    onValueChange = { onPastSurgeriesChange(it) },
                    label = {
                        Text(
                            text = stringResource(CoreR.string.past_surgeries),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.past_surgeries_hint)) },
                    singleLine = false,
                    maxLines = 3,
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

                TextField(
                    value = password,
                    onValueChange = { onPasswordChange(it) },
                    label = {
                        Text(
                            text = stringResource(R.string.password),
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
                    trailingIcon = {
                        IconButton(
                            onClick = { onPasswordVisibilityChange() },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_remove_red_eye_24),
                                contentDescription = null,
                                tint = if (passwordVisibility) Blue500 else Color.Gray,
                            )
                        }
                    },
                    visualTransformation =
                        if (passwordVisibility) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                )

                Spacer(modifier = Modifier.height(L_PADDING))

                AnimatedButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = 0.6f),
                    text = stringResource(R.string.register),
                    color = buttonBackgroundColor,
                    onClick = { onRegisterButtonClicked() },
                )

                Spacer(modifier = Modifier.height(L_PADDING))

                Text(
                    text = stringResource(R.string.sign_in_instead),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontFamily = helveticaFamily,
                    color = textColor,
                    modifier =
                        Modifier
                            .clickable { onLoginClicked() },
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier =
                    Modifier.constrainAs(loading) {
                        top.linkTo(parent.top, margin = L_PADDING)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "ar")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
internal fun SignUpContentPreview() {
    Box(modifier = Modifier.background(backgroundColor)) {
        SignUpContent(
            firstName = "",
            lastName = "",
            email = "",
            nationalId = "",
            birthDate = System.currentTimeMillis(),
            selectedGender = 1,
            genderExpanded = false,
            phone = "",
            address = "",
            password = "",
            chronicDiseases = "",
            currentMedications = "",
            allergies = "",
            pastSurgeries = "",
            weight = 0.0,
            passwordVisibility = false,
            isLoading = false,
            onFirstNameChange = {},
            onEmailChange = {},
            onBirthDateChanged = {},
            onGendersExpandedChange = {},
            onGendersDismissRequest = {},
            onGenderClicked = {},
            onNationalIdChange = {},
            onLastNameChanged = {},
            onPhoneChange = {},
            onAddressChange = {},
            onPasswordChange = {},
            onChronicDiseasesChange = {},
            onCurrentMedicationsChange = {},
            onAllergiesChange = {},
            onPastSurgeriesChange = {},
            onWeightChange = {},
            onPasswordVisibilityChange = {},
            onRegisterButtonClicked = {},
            onLoginClicked = {},
        )
    }
}
