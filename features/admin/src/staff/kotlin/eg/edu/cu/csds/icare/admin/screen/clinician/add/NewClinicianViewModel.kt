package eg.edu.cu.csds.icare.admin.screen.clinician.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.clinician.ClinicianIntent
import eg.edu.cu.csds.icare.admin.screen.clinician.ClinicianSingleEvent
import eg.edu.cu.csds.icare.admin.screen.clinician.ClinicianSingleEvent.ShowError
import eg.edu.cu.csds.icare.admin.screen.clinician.ClinicianState
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.AddNewClinicianCase
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
class NewClinicianViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listClinicsUseCase: ListClinicsUseCase,
    private val addNewClinicianCase: AddNewClinicianCase,
) : ViewModel() {
    private var addClinicianJob: Job? = null
    private val _state = MutableStateFlow(ClinicianState())
    val state =
        _state
            .onStart {
                fetchClinics()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<ClinicianSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: ClinicianIntent) {
        when (intent) {
            is ClinicianIntent.UpdateFirstName ->
                _state.update { it.copy(firstName = intent.firstName) }

            is ClinicianIntent.UpdateLastName ->
                _state.update { it.copy(lastName = intent.lastName) }

            is ClinicianIntent.UpdateClinicId ->
                _state.update { it.copy(clinicId = intent.clinicId) }

            is ClinicianIntent.UpdateClinicsExpanded -> {
                _state.update { it.copy(isClinicsExpanded = intent.isExpanded) }
            }

            is ClinicianIntent.UpdateEmail ->
                _state.update { it.copy(email = intent.email) }

            is ClinicianIntent.UpdatePhone ->
                _state.update { it.copy(phone = intent.phone) }

            is ClinicianIntent.UpdateProfilePicture ->
                _state.update { it.copy(profilePicture = intent.profilePicture) }

            is ClinicianIntent.SelectClinician -> Unit

            is ClinicianIntent.Proceed ->
                viewModelScope.launch {
                    when {
                        _state.value.firstName.isBlank() -> {
                            _singleEvent.emit(
                                ShowError(
                                    message = StringResourceId(R.string.error_first_name),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.lastName.isBlank() -> {
                            _singleEvent.emit(
                                ShowError(
                                    message = StringResourceId(R.string.error_last_name),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.clinicId == 0.toLong() -> {
                            _singleEvent.emit(
                                ShowError(
                                    message = StringResourceId(R.string.error_clinic),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        !_state.value.email.isValidEmail -> {
                            _singleEvent.emit(
                                ShowError(
                                    message = StringResourceId(R.string.error_invalid_email),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.phone.isBlank() ||
                            _state.value.phone.length < Constants.PHONE_LENGTH -> {
                            _singleEvent.emit(
                                ShowError(
                                    message = StringResourceId(R.string.error_phone),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        else -> {
                            addClinicianJob?.cancel()
                            addClinicianJob = launchAddNewClinician()
                        }
                    }
                }
        }
    }

    private fun launchAddNewClinician() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            val clinician =
                Clinician(
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    clinicId = _state.value.clinicId,
                    email = _state.value.email,
                    phone = _state.value.phone,
                    profilePicture = _state.value.profilePicture,
                )
            addNewClinicianCase(clinician)
                .onEach { result ->
                    result
                        .onSuccess {
                            _singleEvent.emit(ClinicianSingleEvent.ShowSuccess)
                            _state.update { it.copy(isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(ClinicianSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }

    private fun fetchClinics() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            listClinicsUseCase()
                .onEach { result ->
                    result
                        .onSuccess { clinics ->
                            _state.update { it.copy(clinics = clinics, isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(ClinicianSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
