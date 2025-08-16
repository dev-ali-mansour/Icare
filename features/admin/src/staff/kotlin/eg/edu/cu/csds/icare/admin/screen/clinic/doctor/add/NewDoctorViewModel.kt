package eg.edu.cu.csds.icare.admin.screen.clinic.doctor.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.clinic.doctor.DoctorIntent
import eg.edu.cu.csds.icare.admin.screen.clinic.doctor.DoctorSingleEvent
import eg.edu.cu.csds.icare.admin.screen.clinic.doctor.DoctorState
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
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
    private val _state = MutableStateFlow(DoctorState())
    val state =
        _state
            .onStart {
                fetchClinics()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<DoctorSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: DoctorIntent) {
        when (intent) {
            is DoctorIntent.UpdateFirstName -> {
                _state.update { it.copy(firstName = intent.firstName) }
            }
            is DoctorIntent.UpdateLastName -> {
                _state.update { it.copy(lastName = intent.lastName) }
            }
            is DoctorIntent.UpdateClinicId -> {
                _state.update { it.copy(clinicId = intent.clinicId) }
            }
            is DoctorIntent.UpdateClinicsExpanded -> {
                _state.update { it.copy(isClinicsExpanded = intent.isExpanded) }
            }
            is DoctorIntent.UpdateEmail -> {
                _state.update { it.copy(email = intent.email) }
            }

            is DoctorIntent.UpdatePhone -> {
                _state.update { it.copy(phone = intent.phone) }
            }
            is DoctorIntent.UpdateSpeciality -> {
                _state.update { it.copy(speciality = intent.speciality) }
            }
            is DoctorIntent.UpdateFromTime -> {
                _state.update { it.copy(fromTime = intent.fromTime) }
            }
            is DoctorIntent.UpdateToTime -> {
                _state.update { it.copy(toTime = intent.toTime) }
            }
            is DoctorIntent.UpdateRating -> {
                _state.update { it.copy(rating = intent.rating) }
            }
            is DoctorIntent.UpdatePrice -> {
                _state.update { it.copy(price = intent.price) }
            }
            is DoctorIntent.UpdateProfilePicture -> {
                _state.update { it.copy(profilePicture = intent.profilePicture) }
            }
            is DoctorIntent.SelectDoctor -> Unit
            is DoctorIntent.Proceed ->
                viewModelScope.launch {
                    when {
                        _state.value.firstName.isBlank() -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.first_name_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.lastName.isBlank() -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.last_name_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.clinicId == 0.toLong() -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.clinic_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        !_state.value.email.isValidEmail -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.email_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.phone.isBlank() ||
                            _state.value.phone.length < Constants.PHONE_LENGTH -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.phone_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.speciality.isBlank() -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.speciality_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.price < 1.0 -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.service_price_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.fromTime == _state.value.toTime -> {
                            _singleEvent.emit(
                                DoctorSingleEvent
                                    .ShowError(message = StringResourceId(R.string.doctor_times_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        else -> {
                            addDoctorJob?.cancel()
                            addDoctorJob = launchAddNewDoctor()
                        }
                    }
                }
        }
    }

    private fun launchAddNewDoctor() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            val doctor =
                Doctor(
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    clinicId = _state.value.clinicId,
                    email = _state.value.email,
                    phone = _state.value.phone,
                    specialty = _state.value.speciality,
                    fromTime = _state.value.fromTime,
                    toTime = _state.value.toTime,
                    rating = _state.value.rating,
                    price = _state.value.price,
                    profilePicture = _state.value.profilePicture,
                )
            addNewDoctorUseCase(doctor)
                .onEach { result ->
                    result
                        .onSuccess {
                            _singleEvent.emit(DoctorSingleEvent.ShowSuccess)
                            _state.update { it.copy(isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(DoctorSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }

    private fun fetchClinics() =
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            listClinicsUseCase()
                .onEach { result ->
                    result
                        .onSuccess { clinics ->
                            _state.update { it.copy(clinics = clinics, isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(DoctorSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
