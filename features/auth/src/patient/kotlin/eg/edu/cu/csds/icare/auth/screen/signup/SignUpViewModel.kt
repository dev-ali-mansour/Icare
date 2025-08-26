package eg.edu.cu.csds.icare.auth.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOutUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignUpUseCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.domain.util.isValidPassword
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
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

    private val _state = MutableStateFlow(SignUpState())
    val state =
        _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _state.value,
        )
    private val _singleEvent = MutableSharedFlow<SignUpSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processEvent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.UpdateFirstName -> {
                _state.update { it.copy(firstName = intent.firstName) }
            }

            is SignUpIntent.UpdateLastName -> {
                _state.value = _state.value.copy(lastName = intent.lastName)
            }

            is SignUpIntent.UpdateEmail -> {
                _state.update { it.copy(email = intent.email) }
            }

            is SignUpIntent.UpdateBirthDate -> {
                _state.update { _state.value.copy(birthDate = intent.birthDate) }
            }

            is SignUpIntent.UpdateGender -> {
                _state.update { it.copy(gender = intent.gender) }
            }

            is SignUpIntent.UpdateGenderExpanded -> {
                _state.update { it.copy(isGenderExpanded = intent.isExpanded) }
            }

            is SignUpIntent.UpdateNationalId -> {
                _state.update { it.copy(nationalId = intent.nationalId) }
            }

            is SignUpIntent.UpdatePhone -> {
                _state.update { it.copy(phone = intent.phone) }
            }

            is SignUpIntent.UpdateAddress -> {
                _state.update { it.copy(address = intent.address) }
            }

            is SignUpIntent.UpdateWeight -> {
                _state.update { it.copy(weight = intent.weight) }
            }

            is SignUpIntent.UpdateChronicDiseases -> {
                _state.update { it.copy(chronicDiseases = intent.chronicDiseases) }
            }

            is SignUpIntent.UpdateCurrentMedications -> {
                _state.update { it.copy(currentMedications = intent.currentMedications) }
            }

            is SignUpIntent.UpdateAllergies -> {
                _state.update { it.copy(allergies = intent.allergies) }
            }

            is SignUpIntent.UpdatePastSurgeries -> {
                _state.update { it.copy(pastSurgeries = intent.pastSurgeries) }
            }

            is SignUpIntent.UpdatePassword -> {
                _state.update { it.copy(password = intent.password) }
            }

            is SignUpIntent.ToggleShowAlert -> {
                _state.update { it.copy(showAlert = !it.showAlert) }
            }

            is SignUpIntent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is SignUpIntent.SubmitSignUp ->
                viewModelScope.launch {
                    when {
                        _state.value.firstName.isBlank() -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_blank_first_name,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.lastName.isBlank() -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_blank_last_name,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        !_state.value.email.isValidEmail -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_invalid_email,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.gender == 0.toShort() -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_invalid_gender,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.phone.isBlank() ||
                            _state.value.phone.length < Constants.PHONE_LENGTH -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_invalid_phone,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.chronicDiseases.isBlank() -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_blank_chronic_diseases,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.currentMedications.isBlank() -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_blank_current_medications,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.allergies.isBlank() -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_blank_allergies,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.pastSurgeries.isBlank() -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_blank_past_surgeries,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        !_state.value.password.isValidPassword -> {
                            _singleEvent.emit(
                                SignUpSingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.features_auth_error_invalid_password,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        else -> {
                            signUpJob?.cancel()
                            signUpJob = launchSignUp()
                        }
                    }
                }

            is SignUpIntent.NavigateToSignInScreen -> Unit
        }
    }

    private fun launchSignUp() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            signUpUseCase(
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                email = _state.value.email,
                birthDate = _state.value.birthDate,
                gender = if (_state.value.gender == 1.toShort()) "M" else "F",
                nationalId = _state.value.nationalId,
                phone = _state.value.phone,
                address = _state.value.address,
                weight = _state.value.weight,
                chronicDiseases = _state.value.chronicDiseases,
                currentMedications = _state.value.currentMedications,
                allergies = _state.value.allergies,
                pastSurgeries = _state.value.pastSurgeries,
                password = _state.value.password,
            ).onEach { result ->
                result
                    .onSuccess {
                        signOutJob?.cancel()
                        signOutJob =
                            signOutUseCase()
                                .onEach { signOutResult ->
                                    signOutResult
                                        .onSuccess {
                                            _singleEvent.emit(
                                                SignUpSingleEvent
                                                    .ShowInfo(
                                                        message =
                                                            StringResourceId(
                                                                R.string.features_auth_register_message,
                                                            ),
                                                    ),
                                            )
                                            _state.update { it.copy(isLoading = false) }
                                        }.onError { error ->
                                            _singleEvent.emit(
                                                SignUpSingleEvent
                                                    .ShowError(message = error.toUiText()),
                                            )
                                            _state.update { it.copy(isLoading = false) }
                                        }
                                }.launchIn(viewModelScope)
                    }.onError { error ->
                        _singleEvent.emit(
                            SignUpSingleEvent
                                .ShowError(message = error.toUiText()),
                        )
                        _state.update { it.copy(isLoading = false) }
                    }
            }.launchIn(viewModelScope)
        }
}
