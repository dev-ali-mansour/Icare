package eg.edu.cu.csds.icare.feature.auth.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOutUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignUpUseCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.domain.util.isValidPassword
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.feature.auth.R
import eg.edu.cu.csds.icare.feature.auth.screen.signup.SignUpEffect.ShowError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignUpViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private var signUpJob: Job? = null
    private var signOutJob: Job? = null

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState =
        _uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _uiState.value,
        )

    val effect = uiState.map { it.effect }

    fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.UpdateFirstName -> {
                _uiState.update { it.copy(firstName = intent.firstName) }
            }

            is SignUpIntent.UpdateLastName -> {
                _uiState.value = _uiState.value.copy(lastName = intent.lastName)
            }

            is SignUpIntent.UpdateEmail -> {
                _uiState.update { it.copy(email = intent.email) }
            }

            is SignUpIntent.UpdateBirthDate -> {
                _uiState.update { _uiState.value.copy(birthDate = intent.birthDate) }
            }

            is SignUpIntent.UpdateGender -> {
                _uiState.update { it.copy(gender = intent.gender) }
            }

            is SignUpIntent.UpdateGenderExpanded -> {
                _uiState.update { it.copy(isGenderExpanded = intent.isExpanded) }
            }

            is SignUpIntent.UpdateNationalId -> {
                _uiState.update { it.copy(nationalId = intent.nationalId) }
            }

            is SignUpIntent.UpdatePhone -> {
                _uiState.update { it.copy(phone = intent.phone) }
            }

            is SignUpIntent.UpdateAddress -> {
                _uiState.update { it.copy(address = intent.address) }
            }

            is SignUpIntent.UpdateWeight -> {
                _uiState.update { it.copy(weight = intent.weight) }
            }

            is SignUpIntent.UpdateChronicDiseases -> {
                _uiState.update { it.copy(chronicDiseases = intent.chronicDiseases) }
            }

            is SignUpIntent.UpdateCurrentMedications -> {
                _uiState.update { it.copy(currentMedications = intent.currentMedications) }
            }

            is SignUpIntent.UpdateAllergies -> {
                _uiState.update { it.copy(allergies = intent.allergies) }
            }

            is SignUpIntent.UpdatePastSurgeries -> {
                _uiState.update { it.copy(pastSurgeries = intent.pastSurgeries) }
            }

            is SignUpIntent.UpdatePassword -> {
                _uiState.update { it.copy(password = intent.password) }
            }

            is SignUpIntent.ToggleShowAlert -> {
                _uiState.update { it.copy(showAlert = !it.showAlert) }
            }

            is SignUpIntent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is SignUpIntent.SubmitSignUp -> {
                viewModelScope.launch {
                    when {
                        _uiState.value.firstName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_error_blank_first_name,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.lastName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_error_blank_last_name,
                                                ),
                                        ),
                                )
                            }
                        }

                        !_uiState.value.email.isValidEmail -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(R.string.feature_auth_error_invalid_email),
                                        ),
                                )
                            }
                        }

                        _uiState.value.gender == 0.toShort() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(R.string.feature_auth_error_invalid_gender),
                                        ),
                                )
                            }
                        }

                        _uiState.value.phone.isBlank() ||
                            _uiState.value.phone.length < Constants.PHONE_LENGTH -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(R.string.feature_auth_error_invalid_phone),
                                        ),
                                )
                            }
                        }

                        _uiState.value.chronicDiseases.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_error_blank_chronic_diseases,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.currentMedications.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_error_blank_current_medications,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.allergies.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(R.string.feature_auth_error_blank_allergies),
                                        ),
                                )
                            }
                        }

                        _uiState.value.pastSurgeries.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_error_blank_past_surgeries,
                                                ),
                                        ),
                                )
                            }
                        }

                        !_uiState.value.password.isValidPassword -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_error_invalid_password,
                                                ),
                                        ),
                                )
                            }
                        }

                        else -> {
                            signUpJob?.cancel()
                            signUpJob = launchSignUp()
                        }
                    }
                }
            }

            is SignUpIntent.NavigateToSignInScreen -> {}

            SignUpIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun launchSignUp() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            signUpUseCase(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                email = _uiState.value.email,
                birthDate = _uiState.value.birthDate,
                gender = if (_uiState.value.gender == 1.toShort()) "M" else "F",
                nationalId = _uiState.value.nationalId,
                phone = _uiState.value.phone,
                address = _uiState.value.address,
                weight = _uiState.value.weight,
                chronicDiseases = _uiState.value.chronicDiseases,
                currentMedications = _uiState.value.currentMedications,
                allergies = _uiState.value.allergies,
                pastSurgeries = _uiState.value.pastSurgeries,
                password = _uiState.value.password,
            ).onEach { result ->
                result
                    .onSuccess {
                        signOutJob?.cancel()
                        signOutJob =
                            signOutUseCase()
                                .onEach { signOutResult ->
                                    signOutResult
                                        .onSuccess {
                                            _uiState.update {
                                                it.copy(
                                                    isLoading = false,
                                                    effect =
                                                        SignUpEffect.ShowInfo(
                                                            message =
                                                                StringResourceId(
                                                                    R.string.feature_auth_register_message,
                                                                ),
                                                        ),
                                                )
                                            }
                                        }.onError { error ->
                                            _uiState.update {
                                                it.copy(
                                                    isLoading = false,
                                                    effect =
                                                        SignUpEffect.ShowError(
                                                            message = error.toUiText(),
                                                        ),
                                                )
                                            }
                                        }
                                }.launchIn(viewModelScope)
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = SignUpEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }.launchIn(viewModelScope)
        }
}
