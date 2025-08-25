package eg.edu.cu.csds.icare.admin.screen.clinic.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicIntent
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicSingleEvent
import eg.edu.cu.csds.icare.admin.screen.clinic.ClinicState
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.AddNewClinicUseCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
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
class NewClinicViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewClinicUseCase: AddNewClinicUseCase,
) : ViewModel() {
    private var addClinicJob: Job? = null
    private val _state = MutableStateFlow(ClinicState())
    val state =
        _state
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<ClinicSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: ClinicIntent) {
        when (intent) {
            is ClinicIntent.UpdateName -> {
                _state.update { it.copy(name = intent.name) }
            }

            is ClinicIntent.UpdateType -> {
                _state.update { it.copy(type = intent.type) }
            }

            is ClinicIntent.UpdatePhone -> {
                _state.update { it.copy(phone = intent.phone) }
            }

            is ClinicIntent.UpdateAddress -> {
                _state.update { it.copy(address = intent.address) }
            }

            is ClinicIntent.UpdateIsOpen -> {
                _state.update { it.copy(isOpen = intent.isOpen) }
            }

            is ClinicIntent.SelectClinic -> Unit
            is ClinicIntent.Proceed ->
                viewModelScope.launch {
                    when {
                        _state.value.name.isBlank() -> {
                            _singleEvent.emit(
                                ClinicSingleEvent.ShowError(
                                    message = StringResourceId(R.string.name_error),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.type.isBlank() -> {
                            _singleEvent.emit(
                                ClinicSingleEvent.ShowError(
                                    message = StringResourceId(R.string.error_type),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.phone.isBlank() ||
                            _state.value.phone.length < Constants.PHONE_LENGTH -> {
                            _singleEvent.emit(
                                ClinicSingleEvent.ShowError(
                                    message = StringResourceId(R.string.error_phone),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.address.isBlank() -> {
                            _singleEvent.emit(
                                ClinicSingleEvent.ShowError(
                                    message = StringResourceId(R.string.error_address),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        else -> {
                            addClinicJob?.cancel()
                            addClinicJob = launchAddNewClinic()
                        }
                    }
                }
        }
    }

    private fun launchAddNewClinic() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            val clinic =
                Clinic(
                    name = _state.value.name,
                    type = _state.value.type,
                    phone = _state.value.phone,
                    address = _state.value.address,
                    isOpen = _state.value.isOpen,
                )
            addNewClinicUseCase(clinic)
                .onEach { result ->
                    result
                        .onSuccess {
                            _singleEvent.emit(ClinicSingleEvent.ShowSuccess)
                            _state.update { it.copy(isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(ClinicSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
