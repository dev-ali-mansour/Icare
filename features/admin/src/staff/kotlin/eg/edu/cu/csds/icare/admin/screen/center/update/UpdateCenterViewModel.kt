package eg.edu.cu.csds.icare.admin.screen.center.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.center.CenterEffect
import eg.edu.cu.csds.icare.admin.screen.center.CenterEvent
import eg.edu.cu.csds.icare.admin.screen.center.CenterState
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.UpdateCenterUseCase
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
class UpdateCenterViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val updateCenterUseCase: UpdateCenterUseCase,
) : ViewModel() {
    private var updateCenterJob: Job? = null
    private val _uiState = MutableStateFlow(CenterState())
    val uiState =
        _uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _uiState.value,
        )
    val effect = uiState.map { it.effect }

    fun processEvent(event: CenterEvent) {
        when (event) {
            is CenterEvent.UpdateName ->
                _uiState.update { it.copy(name = event.name) }

            is CenterEvent.UpdateType ->
                _uiState.update { it.copy(type = event.type) }

            is CenterEvent.UpdateTypesExpanded ->
                _uiState.update { it.copy(isTypesExpanded = event.expanded) }

            is CenterEvent.UpdatePhone ->
                _uiState.update { it.copy(phone = event.phone) }

            is CenterEvent.UpdateAddress ->
                _uiState.update { it.copy(address = event.address) }

            is CenterEvent.LoadCenter -> {
                _uiState.update {
                    it.copy(
                        id = event.center.id,
                        name = event.center.name,
                        type = event.center.type,
                        phone = event.center.phone,
                        address = event.center.address,
                    )
                }
            }

            is CenterEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.name.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        CenterEffect.ShowError(
                                            message = StringResourceId(R.string.error_first_name),
                                        ),
                                )
                            }
                        }

                        uiState.value.type < 1 ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        CenterEffect.ShowError(
                                            message = StringResourceId(R.string.error_type),
                                        ),
                                )
                            }

                        uiState.value.phone.isBlank() ||
                            uiState.value.phone.length < Constants.PHONE_LENGTH ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        CenterEffect.ShowError(
                                            message = StringResourceId(R.string.error_phone),
                                        ),
                                )
                            }

                        uiState.value.address.isBlank() ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        CenterEffect.ShowError(
                                            message = StringResourceId(R.string.error_address),
                                        ),
                                )
                            }

                        else -> {
                            updateCenterJob?.cancel()
                            updateCenterJob = launchUpdateCenter()
                        }
                    }
                }

            CenterEvent.ConsumeEffect -> consumeEffect()
        }
    }

    private fun launchUpdateCenter() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val center =
                LabImagingCenter(
                    id = _uiState.value.id,
                    name = _uiState.value.name,
                    type = _uiState.value.type,
                    phone = _uiState.value.phone,
                    address = _uiState.value.address,
                )
            updateCenterUseCase(center)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = CenterEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = CenterEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun consumeEffect() {
        _uiState.update { it.copy(effect = null) }
    }
}
