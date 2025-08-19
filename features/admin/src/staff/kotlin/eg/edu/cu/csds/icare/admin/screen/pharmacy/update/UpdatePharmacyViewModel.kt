package eg.edu.cu.csds.icare.admin.screen.pharmacy.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyIntent
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacySingleEvent
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyState
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.UpdatePharmacyUseCase
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
class UpdatePharmacyViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val updatePharmacyUseCase: UpdatePharmacyUseCase,
) : ViewModel() {
    private var updatePharmacyJob: Job? = null
    private val _state = MutableStateFlow(PharmacyState())
    val state =
        _state
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<PharmacySingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: PharmacyIntent) {
        when (intent) {
            is PharmacyIntent.UpdateName -> {
                _state.update { it.copy(name = intent.name) }
            }

            is PharmacyIntent.UpdatePhone -> {
                _state.update { it.copy(phone = intent.phone) }
            }

            is PharmacyIntent.UpdateAddress -> {
                _state.update { it.copy(address = intent.address) }
            }

            is PharmacyIntent.SelectPharmacy -> {
                _state.update {
                    it.copy(
                        id = intent.pharmacy.id,
                        name = intent.pharmacy.name,
                        phone = intent.pharmacy.phone,
                        address = intent.pharmacy.address,
                    )
                }
            }

            is PharmacyIntent.Proceed ->
                viewModelScope.launch {
                    when {
                        _state.value.name.isBlank() -> {
                            _singleEvent.emit(
                                PharmacySingleEvent.ShowError(
                                    message = StringResourceId(R.string.name_error),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.phone.isBlank() ||
                            _state.value.phone.length < Constants.PHONE_LENGTH -> {
                            _singleEvent.emit(
                                PharmacySingleEvent.ShowError(
                                    message = StringResourceId(R.string.error_phone),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.address.isBlank() ||
                            _state.value.phone.length < Constants.PHONE_LENGTH -> {
                            _singleEvent.emit(
                                PharmacySingleEvent.ShowError(
                                    message = StringResourceId(R.string.address_error),
                                ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        else -> {
                            updatePharmacyJob?.cancel()
                            updatePharmacyJob = launchUpdatePharmacy()
                        }
                    }
                }
        }
    }

    private fun launchUpdatePharmacy() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            val pharmacy =
                Pharmacy(
                    id = _state.value.id,
                    name = _state.value.name,
                    phone = _state.value.phone,
                    address = _state.value.address,
                )
            updatePharmacyUseCase(pharmacy)
                .onEach { result ->
                    result
                        .onSuccess {
                            _singleEvent.emit(PharmacySingleEvent.ShowSuccess)
                            _state.update { it.copy(isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(PharmacySingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
