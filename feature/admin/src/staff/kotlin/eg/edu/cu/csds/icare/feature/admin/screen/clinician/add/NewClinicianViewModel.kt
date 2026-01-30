package eg.edu.cu.csds.icare.feature.admin.screen.clinician.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.AddNewClinicianCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.feature.admin.R
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.ClinicianEffect
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.ClinicianIntent
import eg.edu.cu.csds.icare.feature.admin.screen.clinician.ClinicianState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NewClinicianViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listClinicsUseCase: ListClinicsUseCase,
    private val addNewClinicianCase: AddNewClinicianCase,
) : ViewModel() {
    private var addClinicianJob: Job? = null
    private val _uiState = MutableStateFlow(ClinicianState())
    val uiState =
        _uiState
            .onStart {
                fetchClinics()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = _uiState.map { it.effect }

    fun handleIntent(intent: ClinicianIntent) {
        when (intent) {
            is ClinicianIntent.UpdateFirstName -> {
                _uiState.update { it.copy(firstName = intent.firstName) }
            }

            is ClinicianIntent.UpdateLastName -> {
                _uiState.update { it.copy(lastName = intent.lastName) }
            }

            is ClinicianIntent.UpdateClinicId -> {
                _uiState.update { it.copy(clinicId = intent.clinicId) }
            }

            is ClinicianIntent.UpdateClinicsExpanded -> {
                _uiState.update { it.copy(isClinicsExpanded = intent.isExpanded) }
            }

            is ClinicianIntent.UpdateEmail -> {
                _uiState.update { it.copy(email = intent.email) }
            }

            is ClinicianIntent.UpdatePhone -> {
                _uiState.update { it.copy(phone = intent.phone) }
            }

            is ClinicianIntent.UpdateProfilePicture -> {
                _uiState.update { it.copy(profilePicture = intent.profilePicture) }
            }

            is ClinicianIntent.LoadClinician -> {
                Unit
            }

            is ClinicianIntent.Proceed -> {
                viewModelScope.launch {
                    when {
                        _uiState.value.firstName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ClinicianEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_first_name,
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
                                        ClinicianEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_last_name,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.clinicId == 0.toLong() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ClinicianEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_clinic),
                                        ),
                                )
                            }
                        }

                        !_uiState.value.email.isValidEmail -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ClinicianEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_invalid_email,
                                                ),
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
                                        ClinicianEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_phone),
                                        ),
                                )
                            }
                        }

                        else -> {
                            addClinicianJob?.cancel()
                            addClinicianJob = launchAddNewClinician()
                        }
                    }
                }
            }

            ClinicianIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun launchAddNewClinician() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val clinician =
                Clinician(
                    firstName = _uiState.value.firstName,
                    lastName = _uiState.value.lastName,
                    clinicId = _uiState.value.clinicId,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    profilePicture = _uiState.value.profilePicture,
                )
            addNewClinicianCase(clinician)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ClinicianEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ClinicianEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun fetchClinics() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listClinicsUseCase()
                .onEach { result ->
                    result
                        .onSuccess { clinics ->
                            _uiState.update { it.copy(clinics = clinics, isLoading = false) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ClinicianEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
