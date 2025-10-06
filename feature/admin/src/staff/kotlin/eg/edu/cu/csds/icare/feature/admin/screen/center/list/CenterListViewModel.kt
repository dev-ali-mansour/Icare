package eg.edu.cu.csds.icare.feature.admin.screen.center.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
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
class CenterListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listCentersUseCase: ListCentersUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CenterListState())
    val uiState =
        _uiState
            .onStart {
                fetchCenters()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(event: CenterListEvent) {
        when (event) {
            is CenterListEvent.Refresh -> {
                fetchCenters(forceUpdate = true)
            }

            is CenterListEvent.SelectCenter -> {
                _uiState.update {
                    it.copy(effect = CenterListEffect.NavigateToCenterDetails(center = event.center))
                }
            }

            is CenterListEvent.UpdateFabExpanded -> {
                _uiState.update {
                    it.copy(effect = CenterListEffect.UpdateFabExpanded(isExpanded = event.isExpanded))
                }
            }

            CenterListEvent.ConsumeEffect -> consumeEffect()
        }
    }

    private fun fetchCenters(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCentersUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { centers ->
                            _uiState.update { it.copy(isLoading = false, centers = centers) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = CenterListEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun consumeEffect() {
        _uiState.update { it.copy(effect = null) }
    }
}
