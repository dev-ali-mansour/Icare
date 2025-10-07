package eg.edu.cu.csds.icare.feature.admin.screen.clinic.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.feature.admin.R
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.ClinicEffect
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.ClinicEvent
import eg.edu.cu.csds.icare.feature.admin.screen.clinic.ClinicState
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.UpdateClinicUseCase
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
class UpdateClinicViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val updateClinicUseCase: UpdateClinicUseCase,
) : ViewModel() {
    private var updateClinicJob: Job? = null
    private val _uiState = MutableStateFlow(ClinicState())
    val uiState =
        _uiState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(event: ClinicEvent) {
        when (event) {
            is ClinicEvent.UpdateName -> {
                _uiState.update { it.copy(name = event.name) }
            }

            is ClinicEvent.UpdateType -> {
                _uiState.update { it.copy(type = event.type) }
            }

            is ClinicEvent.UpdatePhone -> {
                _uiState.update { it.copy(phone = event.phone) }
            }

            is ClinicEvent.UpdateAddress -> {
                _uiState.update { it.copy(address = event.address) }
            }

            is ClinicEvent.UpdateIsOpen -> {
                _uiState.update { it.copy(isOpen = event.isOpen) }
            }

            is ClinicEvent.LoadClinic -> {
                _uiState.update {
                    it.copy(
                        id = event.clinic.id,
                        name = event.clinic.name,
                        type = event.clinic.type,
                        phone = event.clinic.phone,
                        address = event.clinic.address,
                        isOpen = event.clinic.isOpen,
                    )
                }
            }

            is ClinicEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.name.isBlank() ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ClinicEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_name_error),
                                        ),
                                )
                            }

                        _uiState.value.type.isBlank() ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ClinicEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_type),
                                        ),
                                )
                            }

                        _uiState.value.phone.isBlank() ||
                            _uiState.value.phone.length < Constants.PHONE_LENGTH ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ClinicEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_phone),
                                        ),
                                )
                            }

                        _uiState.value.address.isBlank() ||
                            _uiState.value.phone.length < Constants.PHONE_LENGTH ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ClinicEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_address),
                                        ),
                                )
                            }

                        else -> {
                            updateClinicJob?.cancel()
                            updateClinicJob = launchUpdateClinic()
                        }
                    }
                }

            is ClinicEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchUpdateClinic() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val clinic =
                Clinic(
                    id = _uiState.value.id,
                    name = _uiState.value.name,
                    type = _uiState.value.type,
                    phone = _uiState.value.phone,
                    address = _uiState.value.address,
                    isOpen = _uiState.value.isOpen,
                )
            updateClinicUseCase(clinic)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update { it.copy(isLoading = false, effect = ClinicEffect.ShowSuccess) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ClinicEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
