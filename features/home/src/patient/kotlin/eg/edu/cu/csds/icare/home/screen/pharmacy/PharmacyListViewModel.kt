package eg.edu.cu.csds.icare.home.screen.pharmacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
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
class PharmacyListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
) : ViewModel() {
    private var fetchPharmaciesJob: Job? = null
    private val _uiState = MutableStateFlow(PharmacyListState())
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

    fun processEvent(event: PharmacyListEvent) {
        when (event) {
            is PharmacyListEvent.OnBackClick -> {
                _uiState.update { it.copy(effect = PharmacyListEffect.OnBackClick) }
            }

            is PharmacyListEvent.Refresh -> {
                fetchPharmaciesJob?.cancel()
                fetchPharmaciesJob = refreshPharmacies()
            }

            is PharmacyListEvent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }

            is PharmacyListEvent.Search -> {
                fetchPharmaciesJob?.cancel()
                fetchPharmaciesJob = launchSearchPharmacies(query = _uiState.value.searchQuery)
            }

            PharmacyListEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun refreshPharmacies() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listPharmaciesUseCase(forceUpdate = true)
                .onEach { result ->
                    result
                        .onSuccess { pharmacies ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    searchQuery = "",
                                    pharmacies = pharmacies,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = PharmacyListEffect.ShowError(message = error.toUiText()),
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
                fetchPharmaciesJob?.cancel()
                fetchPharmaciesJob = launchSearchPharmacies(query)
            }.launchIn(viewModelScope)
    }

    fun launchSearchPharmacies(query: String) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listPharmaciesUseCase().collect { result ->
                result
                    .onSuccess { pharmacies ->
                        val queryResults =
                            pharmacies.filter { pharmacy ->
                                query.isEmpty() ||
                                    pharmacy.name.contains(query, ignoreCase = true) ||
                                    pharmacy.address.contains(query, ignoreCase = true)
                            }
                        _uiState.update { it.copy(isLoading = false, pharmacies = queryResults) }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = PharmacyListEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }
}
