package eg.edu.cu.csds.icare.feature.home.screen.lab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.ui.common.CenterTypeItem
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LabListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listCentersUseCase: ListCentersUseCase,
) : ViewModel() {
    private var fetchLabsJob: Job? = null
    private val _uiState = MutableStateFlow(LabListState())
    val uiState =
        _uiState
            .onStart {
                observeSearchQuery()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(event: LabListEvent) {
        when (event) {
            is LabListEvent.OnBackClick -> {
                _uiState.update { it.copy(effect = LabListEffect.OnBackClick) }
            }

            is LabListEvent.Refresh -> {
                fetchLabsJob?.cancel()
                fetchLabsJob = refreshLabCenters()
            }

            is LabListEvent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }

            is LabListEvent.Search -> {
                fetchLabsJob?.cancel()
                fetchLabsJob = launchSearchLabCenters(query = _uiState.value.searchQuery)
            }

            LabListEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun refreshLabCenters() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCentersUseCase(forceUpdate = true)
                .onEach { result ->
                    result
                        .onSuccess { centers ->
                            val labs =
                                centers.filter { center ->
                                    center.type == CenterTypeItem.LabCenter.code
                                }
                            _uiState.update { it.copy(isLoading = false, searchQuery = "", labs = labs) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = LabListEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        _uiState
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(timeoutMillis = 500L)
            .onEach { query ->
                fetchLabsJob?.cancel()
                fetchLabsJob = launchSearchLabCenters(query)
            }.launchIn(viewModelScope)
    }

    fun launchSearchLabCenters(query: String) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCentersUseCase().collect { result ->
                result
                    .onSuccess { centers ->
                        val queryResults =
                            centers.filter { center ->
                                center.type == CenterTypeItem.LabCenter.code &&
                                    (
                                        query.isEmpty() ||
                                            center.name.contains(query, ignoreCase = true) ||
                                            center.address.contains(query, ignoreCase = true)
                                    )
                            }
                        _uiState.update { it.copy(isLoading = false, labs = queryResults) }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = LabListEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }
}
