package eg.edu.cu.csds.icare.feature.home.screen.pharmacy

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

    fun handleIntent(intent: PharmacyListIntent) {
        when (intent) {
            is PharmacyListIntent.OnBackClick -> {
                _uiState.update { it.copy(effect = PharmacyListEffect.OnBackClick) }
            }

            is PharmacyListIntent.Refresh -> {
                fetchPharmaciesJob?.cancel()
                fetchPharmaciesJob = refreshPharmacies()
            }

            is PharmacyListIntent.ChangeTopBar -> {
                _uiState.update { it.copy(topBar = intent.topBar) }
            }

            is PharmacyListIntent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
            }

            is PharmacyListIntent.Search -> {
                fetchPharmaciesJob?.cancel()
                fetchPharmaciesJob = launchSearchPharmacies(query = _uiState.value.searchQuery)
            }

            PharmacyListIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
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

    private fun launchSearchPharmacies(query: String) =
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
