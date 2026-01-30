package eg.edu.cu.csds.icare.feature.admin.screen.doctor.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.UpdateDoctorUseCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.feature.admin.R
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.DoctorEffect
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.DoctorIntent
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.DoctorState
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
class UpdateDoctorViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listClinicsUseCase: ListClinicsUseCase,
    private val updateDoctorUseCase: UpdateDoctorUseCase,
) : ViewModel() {
    private var updateDoctorJob: Job? = null
    private val _uiState = MutableStateFlow(DoctorState())
    val uiState =
        _uiState
            .onStart {
                fetchClinics()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun handleIntent(intent: DoctorIntent) {
        when (intent) {
            is DoctorIntent.UpdateFirstName -> {
                _uiState.update { it.copy(firstName = intent.firstName) }
            }

            is DoctorIntent.UpdateLastName -> {
                _uiState.update { it.copy(lastName = intent.lastName) }
            }

            is DoctorIntent.UpdateClinicId -> {
                _uiState.update { it.copy(clinicId = intent.clinicId) }
            }

            is DoctorIntent.UpdateClinicsExpanded -> {
                _uiState.update { it.copy(isClinicsExpanded = intent.isExpanded) }
            }

            is DoctorIntent.UpdateEmail -> {
                _uiState.update { it.copy(email = intent.email) }
            }

            is DoctorIntent.UpdatePhone -> {
                _uiState.update { it.copy(phone = intent.phone) }
            }

            is DoctorIntent.UpdateSpeciality -> {
                _uiState.update { it.copy(speciality = intent.speciality) }
            }

            is DoctorIntent.UpdateFromTime -> {
                _uiState.update { it.copy(fromTime = intent.fromTime) }
            }

            is DoctorIntent.UpdateToTime -> {
                _uiState.update { it.copy(toTime = intent.toTime) }
            }

            is DoctorIntent.UpdateRating -> {
                _uiState.update { it.copy(rating = intent.rating) }
            }

            is DoctorIntent.UpdatePrice -> {
                _uiState.update { it.copy(price = intent.price) }
            }

            is DoctorIntent.UpdateProfilePicture -> {
                _uiState.update { it.copy(profilePicture = intent.profilePicture) }
            }

            is DoctorIntent.LoadDoctor -> {
                _uiState.update {
                    it.copy(
                        id = intent.doctor.id,
                        firstName = intent.doctor.firstName,
                        lastName = intent.doctor.lastName,
                        email = intent.doctor.email,
                        phone = intent.doctor.phone,
                        speciality = intent.doctor.specialty,
                        clinicId = intent.doctor.clinicId,
                        fromTime = intent.doctor.fromTime,
                        toTime = intent.doctor.toTime,
                        rating = intent.doctor.rating,
                        price = intent.doctor.price,
                        profilePicture = intent.doctor.profilePicture,
                    )
                }
            }

            is DoctorIntent.Proceed -> {
                viewModelScope.launch {
                    when {
                        _uiState.value.firstName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        DoctorEffect.ShowError(
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
                                        DoctorEffect.ShowError(
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
                                        DoctorEffect.ShowError(
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
                                        DoctorEffect.ShowError(
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
                                        DoctorEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_phone),
                                        ),
                                )
                            }
                        }

                        _uiState.value.speciality.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        DoctorEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_speciality,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.price < 1.0 -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        DoctorEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_service_price,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.fromTime == _uiState.value.toTime -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        DoctorEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_doctor_times,
                                                ),
                                        ),
                                )
                            }
                        }

                        else -> {
                            updateDoctorJob?.cancel()
                            updateDoctorJob = launchUpdateDoctor()
                        }
                    }
                }
            }

            DoctorIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun launchUpdateDoctor() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val doctor =
                Doctor(
                    id = _uiState.value.id,
                    firstName = _uiState.value.firstName,
                    lastName = _uiState.value.lastName,
                    clinicId = _uiState.value.clinicId,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    specialty = _uiState.value.speciality,
                    fromTime = _uiState.value.fromTime,
                    toTime = _uiState.value.toTime,
                    rating = _uiState.value.rating,
                    price = _uiState.value.price,
                    profilePicture = _uiState.value.profilePicture,
                )
            updateDoctorUseCase(doctor)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = DoctorEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = DoctorEffect.ShowError(message = error.toUiText()),
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
                                    effect = DoctorEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
