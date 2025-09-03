package eg.edu.cu.csds.icare.admin.screen.pharmacy.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyEffect
import eg.edu.cu.csds.icare.admin.screen.pharmacy.PharmacyEvent
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
class UpdatePharmacyViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val updatePharmacyUseCase: UpdatePharmacyUseCase,
) : ViewModel() {
    private var updatePharmacyJob: Job? = null
    private val _uiState = MutableStateFlow(PharmacyState())
    val uiState =
        _uiState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = _uiState.map { it.effect }

    fun processEvent(event: PharmacyEvent) {
        when (event) {
            is PharmacyEvent.UpdateName -> {
                _uiState.update { it.copy(name = event.name) }
            }

            is PharmacyEvent.UpdatePhone -> {
                _uiState.update { it.copy(phone = event.phone) }
            }

            is PharmacyEvent.UpdateAddress -> {
                _uiState.update { it.copy(address = event.address) }
            }

            is PharmacyEvent.LoadPharmacy -> {
                _uiState.update {
                    it.copy(
                        id = event.pharmacy.id,
                        name = event.pharmacy.name,
                        phone = event.pharmacy.phone,
                        address = event.pharmacy.address,
                    )
                }
            }

            is PharmacyEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.name.isBlank() ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PharmacyEffect.ShowError(
                                            message = StringResourceId(R.string.features_admin_name_error),
                                        ),
                                )
                            }

                        _uiState.value.phone.isBlank() ||
                            _uiState.value.phone.length < Constants.PHONE_LENGTH ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PharmacyEffect.ShowError(
                                            message = StringResourceId(R.string.features_admin_error_phone),
                                        ),
                                )
                            }

                        _uiState.value.address.isBlank() ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PharmacyEffect.ShowError(
                                            message = StringResourceId(R.string.features_admin_error_address),
                                        ),
                                )
                            }

                        else -> {
                            updatePharmacyJob?.cancel()
                            updatePharmacyJob = launchUpdatePharmacy()
                        }
                    }
                }

            PharmacyEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchUpdatePharmacy() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val pharmacy =
                Pharmacy(
                    id = _uiState.value.id,
                    name = _uiState.value.name,
                    phone = _uiState.value.phone,
                    address = _uiState.value.address,
                )
            updatePharmacyUseCase(pharmacy)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = PharmacyEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = PharmacyEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
