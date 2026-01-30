package eg.edu.cu.csds.icare.feature.home.screen.imaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.ui.common.CenterTypeItem
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.core.ui.view.TopSearchBarState
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
class ImagingCenterListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listCentersUseCase: ListCentersUseCase,
) : ViewModel() {
    private var fetchCentersJob: Job? = null
    private val _uiState = MutableStateFlow(ImagingCenterListState())
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

    fun handleIntent(intent: ImagingCenterListIntent) {
        when (intent) {
            is ImagingCenterListIntent.OnBackClick -> {
                _uiState.update { it.copy(effect = ImagingCenterListEffect.OnBackClick) }
            }

            is ImagingCenterListIntent.Refresh -> {
                fetchCentersJob?.cancel()
                fetchCentersJob = refreshImagingCenters()
            }

            is ImagingCenterListIntent.UpdateSearchTopBarState -> {
                _uiState.update { it.copy(searchBarState = intent.state) }
                if (intent.state == TopSearchBarState.Collapsed) {
                    fetchCentersJob?.cancel()
                    fetchCentersJob = launchSearchImagingCenters(_uiState.value.searchQuery)
                }
            }

            is ImagingCenterListIntent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
            }

            is ImagingCenterListIntent.Search -> {
                fetchCentersJob?.cancel()
                fetchCentersJob = launchSearchImagingCenters(query = _uiState.value.searchQuery)
            }

            ImagingCenterListIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun refreshImagingCenters() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCentersUseCase(forceUpdate = true)
                .onEach { result ->
                    result
                        .onSuccess { centers ->
                            val imagingCenters =
                                centers.filter { center ->
                                    center.type == CenterTypeItem.ImagingCenter.code
                                }
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    searchQuery = "",
                                    centers = imagingCenters,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ImagingCenterListEffect.ShowError(message = error.toUiText()),
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
                fetchCentersJob?.cancel()
                fetchCentersJob = launchSearchImagingCenters(query)
            }.launchIn(viewModelScope)
    }

    fun launchSearchImagingCenters(query: String) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCentersUseCase().collect { result ->
                result
                    .onSuccess { centers ->
                        val queryResults =
                            centers.filter { center ->
                                center.type == CenterTypeItem.ImagingCenter.code &&
                                    (
                                        query.isEmpty() ||
                                            center.name.contains(query, ignoreCase = true) ||
                                            center.address.contains(query, ignoreCase = true)
                                    )
                            }
                        _uiState.update { it.copy(isLoading = false, centers = queryResults) }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = ImagingCenterListEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }
}
