package eg.edu.cu.csds.icare.feature.auth.screen.signup

import android.content.Context
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
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eg.edu.cu.csds.icare.core.data.util.getFormattedDate
import eg.edu.cu.csds.icare.core.ui.R.drawable
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.common.GenderItem
import eg.edu.cu.csds.icare.core.ui.common.LaunchedUiEffectHandler
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.theme.Blue200
import eg.edu.cu.csds.icare.core.ui.theme.Blue500
import eg.edu.cu.csds.icare.core.ui.theme.IcareTheme
import eg.edu.cu.csds.icare.core.ui.theme.L_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.S_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.TEXT_AREA_HEIGHT
import eg.edu.cu.csds.icare.core.ui.theme.XL4_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.XL_PADDING
import eg.edu.cu.csds.icare.core.ui.theme.Yellow500

import eg.edu.cu.csds.icare.core.ui.theme.buttonBackgroundColor
import eg.edu.cu.csds.icare.core.ui.theme.contentColor
import eg.edu.cu.csds.icare.core.ui.theme.dropDownTextColor
import eg.edu.cu.csds.icare.core.ui.theme.helveticaFamily
import eg.edu.cu.csds.icare.core.ui.theme.textColor
import eg.edu.cu.csds.icare.core.ui.util.tooling.preview.PreviewArabicLightDark
import eg.edu.cu.csds.icare.core.ui.view.AnimatedButton
import eg.edu.cu.csds.icare.feature.auth.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SignUpScreen(
    navigateToScreen: (Route) -> Unit,
    onSignUpSuccess: () -> Unit,
) {
    val viewModel: SignUpViewModel = koinViewModel()
    val context: Context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedUiEffectHandler(
        viewModel.effect,
        onConsumeEffect = { viewModel.handleIntent(SignUpIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is SignUpEffect.SignUpSuccess -> {
                    onSignUpSuccess()
                }

                is SignUpEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }

                is SignUpEffect.ShowInfo -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message.asString(context),
                        duration = SnackbarDuration.Short,
                    )
                }
            }
        },
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.BottomCenter,
        ) {
            SignUpContent(
                state = state,
                onIntent = { intent ->
                    when (intent) {
                        is SignUpIntent.NavigateToSignInScreen -> {
                            navigateToScreen(Route.SignIn)
                        }

                        else -> {
                            viewModel.handleIntent(intent)
                        }
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpContent(
    state: SignUpState,
    onIntent: (SignUpIntent) -> Unit,
) {
    val context: Context = LocalContext.current
    val genders =
        listOf(
            GenderItem(code = 1, textResId = string.core_ui_male),
            GenderItem(code = 2, textResId = string.core_ui_female),
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
                            onIntent(SignUpIntent.UpdateBirthDate(it))
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
                    text = stringResource(R.string.feature_auth_create_account),
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
                        .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = state.firstName,
                    onValueChange = { onIntent(SignUpIntent.UpdateFirstName(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_auth_first_name),
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
                    onValueChange = { onIntent(SignUpIntent.UpdateLastName(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_auth_last_name),
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
                    value = state.email,
                    onValueChange = { onIntent(SignUpIntent.UpdateEmail(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_auth_email),
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
                    value = state.birthDate.getFormattedDate(context),
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(string.core_ui_birth_date),
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
                            painter =
                                painterResource(
                                    id = drawable.core_ui_baseline_calendar_month_24,
                                ),
                            contentDescription = "",
                        )
                    },
                    interactionSource = interactionSource,
                )

                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                    expanded = state.isGenderExpanded,
                    onExpandedChange = { onIntent(SignUpIntent.UpdateGenderExpanded(it)) },
                ) {
                    OutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        value =
                            genders.firstOrNull { it.code == state.gender }?.textResId?.let {
                                stringResource(it)
                            } ?: "",
                        onValueChange = { },
                        label = {
                            Text(
                                text = stringResource(string.core_ui_gender),
                                color = dropDownTextColor,
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = state.isGenderExpanded,
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
                        expanded = state.isGenderExpanded,
                        onDismissRequest = {
                            onIntent(SignUpIntent.UpdateGenderExpanded(false))
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
                                onClick = {
                                    onIntent(SignUpIntent.UpdateGender(it.code))
                                    onIntent(SignUpIntent.UpdateGenderExpanded(false))
                                },
                            )
                        }
                    }
                }

                TextField(
                    value = state.nationalId,
                    onValueChange = { if (it.length < 15) onIntent(SignUpIntent.UpdateNationalId(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_auth_national_id),
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
                    value = state.phone,
                    onValueChange = { if (it.length < 14) onIntent(SignUpIntent.UpdatePhone(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_auth_phone_number),
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
                    value = state.address,
                    onValueChange = { onIntent(SignUpIntent.UpdateAddress(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_auth_address),
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
                    value = state.weight.toString(),
                    onValueChange = {
                        runCatching {
                            onIntent(SignUpIntent.UpdateWeight(if (it.isEmpty()) 0.0 else it.toDouble()))
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(string.core_ui_weight),
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
                    value = state.chronicDiseases,
                    onValueChange = { onIntent(SignUpIntent.UpdateChronicDiseases(it)) },
                    label = {
                        Text(
                            text =
                                stringResource(
                                    string.core_ui_chronic_diseases,
                                ),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.feature_auth_chronic_diseases_hint),
                        )
                    },
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
                    value = state.currentMedications,
                    onValueChange = { onIntent(SignUpIntent.UpdateCurrentMedications(it)) },
                    label = {
                        Text(
                            text = stringResource(string.core_ui_current_medications),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.feature_auth_current_medications_hint),
                        )
                    },
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
                    value = state.allergies,
                    onValueChange = { onIntent(SignUpIntent.UpdateAllergies(it)) },
                    label = {
                        Text(
                            text = stringResource(string.core_ui_allergies),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.feature_auth_allergies_hint)) },
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
                    value = state.pastSurgeries,
                    onValueChange = { onIntent(SignUpIntent.UpdatePastSurgeries(it)) },
                    label = {
                        Text(
                            text = stringResource(string.core_ui_past_surgeries),
                            fontFamily = helveticaFamily,
                            color = textColor,
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.feature_auth_past_surgeries_hint)) },
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
                    value = state.password,
                    onValueChange = { onIntent(SignUpIntent.UpdatePassword(it)) },
                    label = {
                        Text(
                            text = stringResource(R.string.feature_auth_password),
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
                            onClick = { onIntent(SignUpIntent.TogglePasswordVisibility) },
                        ) {
                            Icon(
                                painter =
                                    painterResource(
                                        R.drawable.feature_auth_ic_baseline_remove_red_eye_24,
                                    ),
                                contentDescription = null,
                                tint = if (state.isPasswordVisible) Blue500 else Color.Gray,
                            )
                        }
                    },
                    visualTransformation =
                        if (state.isPasswordVisible) {
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
                    text = stringResource(R.string.feature_auth_sign_up),
                    color = buttonBackgroundColor,
                    onClick = { onIntent(SignUpIntent.SubmitSignUp) },
                )

                Spacer(modifier = Modifier.height(L_PADDING))

                Text(
                    text = stringResource(R.string.feature_auth_sign_in_instead),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontFamily = helveticaFamily,
                    color = textColor,
                    modifier =
                        Modifier
                            .clickable { onIntent(SignUpIntent.NavigateToSignInScreen) },
                )
            }
        }

        if (state.isLoading) {
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

@PreviewLightDark
@PreviewArabicLightDark
@PreviewScreenSizes
@Composable
private fun SignUpContentPreview() {
    IcareTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SignUpContent(
                state = SignUpState(),
                onIntent = {},
            )
        }
    }
}
