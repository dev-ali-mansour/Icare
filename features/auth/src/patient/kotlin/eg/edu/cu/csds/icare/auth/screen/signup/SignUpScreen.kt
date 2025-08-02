package eg.edu.cu.csds.icare.auth.screen.signup

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.auth.screen.AuthViewModel
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.domain.util.isValidPassword
import eg.edu.cu.csds.icare.core.ui.theme.backgroundColor
import eg.edu.cu.csds.icare.core.ui.util.getErrorMessage
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
