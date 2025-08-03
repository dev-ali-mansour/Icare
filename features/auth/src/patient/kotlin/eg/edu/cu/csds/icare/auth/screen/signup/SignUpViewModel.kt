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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@KoinViewModel
class SignUpViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private var signUpJob: Job? = null
    private var signOutJob: Job? = null

    private val _state = MutableStateFlow(SignUpUIState())
    val state =
        _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _state.value,
        )

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.UpdateFirstName -> {
                _state.update { it.copy(firstName = action.firstName) }
            }

            is SignUpAction.UpdateLastName -> {
                _state.value = _state.value.copy(lastName = action.lastName)
            }

            is SignUpAction.UpdateEmail -> {
                _state.update { it.copy(email = action.email) }
            }

            is SignUpAction.UpdateBirthDate -> {
                _state.update { _state.value.copy(birthDate = action.birthDate) }
            }

            is SignUpAction.UpdateGender -> {
                _state.update { it.copy(gender = action.gender) }
            }

            is SignUpAction.UpdateGenderExpanded -> {
                _state.update { it.copy(isGenderExpanded = action.isExpanded) }
            }

            is SignUpAction.UpdateNationalId -> {
                _state.update { it.copy(nationalId = action.nationalId) }
            }

            is SignUpAction.UpdatePhone -> {
                _state.update { it.copy(phone = action.phone) }
            }

            is SignUpAction.UpdateAddress -> {
                _state.update { it.copy(address = action.address) }
            }

            is SignUpAction.UpdateWeight -> {
                _state.update { it.copy(weight = action.weight) }
            }

            is SignUpAction.UpdateChronicDiseases -> {
                _state.update { it.copy(chronicDiseases = action.chronicDiseases) }
            }

            is SignUpAction.UpdateCurrentMedications -> {
                _state.update { it.copy(currentMedications = action.currentMedications) }
            }

            is SignUpAction.UpdateAllergies -> {
                _state.update { it.copy(allergies = action.allergies) }
            }

            is SignUpAction.UpdatePastSurgeries -> {
                _state.update { it.copy(pastSurgeries = action.pastSurgeries) }
            }

            is SignUpAction.UpdatePassword -> {
                _state.update { it.copy(password = action.password) }
            }

            is SignUpAction.ToggleShowAlert -> {
                _state.update { it.copy(showAlert = !it.showAlert) }
            }

            is SignUpAction.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is SignUpAction.SubmitSignUp -> {
                when {
                    _state.value.firstName.isBlank() -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_blank_first_name),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.lastName.isBlank() -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_blank_last_name),
                                isLoading = false,
                            )
                        }
                    }

                    !_state.value.email.isValidEmail -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_invalid_email),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.gender == 0.toShort() -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_invalid_gender),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.nationalId.isBlank() ||
                        _state.value.nationalId.length < Constants.NATIONAL_ID_LENGTH -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(CoreR.string.error_invalid_national_id),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.phone.isBlank() ||
                        _state.value.phone.length < Constants.PHONE_LENGTH -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_invalid_phone),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.chronicDiseases.isBlank() -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_blank_chronic_diseases),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.currentMedications.isBlank() -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_blank_current_medications),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.allergies.isBlank() -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_blank_allergies),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.pastSurgeries.isBlank() -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.err0r_blank_past_surgeries),
                                isLoading = false,
                            )
                        }
                    }

                    !_state.value.password.isValidPassword -> {
                        _state.update {
                            it.copy(
                                alertMessage = StringResourceId(R.string.error_invalid_password),
                                isLoading = false,
                            )
                        }
                    }

                    else -> {
                        signUpJob?.cancel()
                        signUpJob = onSignUp()
                    }
                }
            }

            is SignUpAction.NavigateToSignInScreen -> Unit
        }
    }

    private fun onSignUp() =
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
                                            _state.update {
                                                it.copy(
                                                    isLoading = false,
                                                    signUpSuccess = true,
                                                    alertMessage = StringResourceId(R.string.register_message),
                                                )
                                            }
                                        }.onError { error ->
                                            _state.update {
                                                it.copy(
                                                    isLoading = false,
                                                    alertMessage = error.toUiText(),
                                                )
                                            }
                                        }
                                }.launchIn(viewModelScope)
                    }.onError { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                alertMessage = error.toUiText(),
                            )
                        }
                    }
            }.launchIn(viewModelScope)
        }
}
