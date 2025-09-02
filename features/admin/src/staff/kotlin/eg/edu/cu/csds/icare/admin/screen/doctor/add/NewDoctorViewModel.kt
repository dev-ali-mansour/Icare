package eg.edu.cu.csds.icare.admin.screen.doctor.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.doctor.DoctorEffect
import eg.edu.cu.csds.icare.admin.screen.doctor.DoctorEvent
import eg.edu.cu.csds.icare.admin.screen.doctor.DoctorState
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.AddNewDoctorUseCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
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
class NewDoctorViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listClinicsUseCase: ListClinicsUseCase,
    private val addNewDoctorUseCase: AddNewDoctorUseCase,
) : ViewModel() {
    private var addDoctorJob: Job? = null
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

    fun processEvent(event: DoctorEvent) {
        when (event) {
            is DoctorEvent.UpdateFirstName -> {
                _uiState.update { it.copy(firstName = event.firstName) }
            }

            is DoctorEvent.UpdateLastName -> {
                _uiState.update { it.copy(lastName = event.lastName) }
            }

            is DoctorEvent.UpdateClinicId -> {
                _uiState.update { it.copy(clinicId = event.clinicId) }
            }

            is DoctorEvent.UpdateClinicsExpanded -> {
                _uiState.update { it.copy(isClinicsExpanded = event.isExpanded) }
            }

            is DoctorEvent.UpdateEmail -> {
                _uiState.update { it.copy(email = event.email) }
            }

            is DoctorEvent.UpdatePhone -> {
                _uiState.update { it.copy(phone = event.phone) }
            }

            is DoctorEvent.UpdateSpeciality -> {
                _uiState.update { it.copy(speciality = event.speciality) }
            }

            is DoctorEvent.UpdateFromTime -> {
                _uiState.update { it.copy(fromTime = event.fromTime) }
            }

            is DoctorEvent.UpdateToTime -> {
                _uiState.update { it.copy(toTime = event.toTime) }
            }

            is DoctorEvent.UpdateRating -> {
                _uiState.update { it.copy(rating = event.rating) }
            }

            is DoctorEvent.UpdatePrice -> {
                _uiState.update { it.copy(price = event.price) }
            }

            is DoctorEvent.UpdateProfilePicture -> {
                _uiState.update { it.copy(profilePicture = event.profilePicture) }
            }

            is DoctorEvent.LoadDoctor -> Unit
            is DoctorEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.firstName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        DoctorEffect.ShowError(
                                            message = StringResourceId(R.string.error_first_name),
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
                                            message = StringResourceId(R.string.error_last_name),
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
                                            message = StringResourceId(R.string.error_clinic),
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
                                            message = StringResourceId(R.string.error_invalid_email),
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
                                            message = StringResourceId(R.string.error_phone),
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
                                            message = StringResourceId(R.string.speciality_error),
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
                                            message = StringResourceId(R.string.service_price_error),
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
                                            message = StringResourceId(R.string.doctor_times_error),
                                        ),
                                )
                            }
                        }

                        else -> {
                            addDoctorJob?.cancel()
                            addDoctorJob = launchAddNewDoctor()
                        }
                    }
                }

            DoctorEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchAddNewDoctor() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val doctor =
                Doctor(
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
            addNewDoctorUseCase(doctor)
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
