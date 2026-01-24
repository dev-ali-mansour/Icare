package eg.edu.cu.csds.icare.feature.appointment.screen.doctors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
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
class DoctorListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listDoctorsUseCase: ListDoctorsUseCase,
) : ViewModel() {
    private var fetchDoctorsJob: Job? = null
    private val _uiState = MutableStateFlow(DoctorListState())
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

    fun handleIntent(intent: DoctorListIntent) {
        when (intent) {
            is DoctorListIntent.OnBackClick -> {
                _uiState.update { it.copy(effect = DoctorListEffect.OnBackClick) }
            }

            is DoctorListIntent.Refresh -> {
                fetchDoctorsJob?.cancel()
                fetchDoctorsJob = refreshDoctors()
            }

            is DoctorListIntent.UpdateSearchTopBarState -> {
                _uiState.update { it.copy(searchBarState = intent.state) }
                if (intent.state == TopSearchBarState.Collapsed) {
                    fetchDoctorsJob?.cancel()
                    fetchDoctorsJob = launchSearchDoctors(_uiState.value.searchQuery)
                }
            }

            is DoctorListIntent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
            }

            is DoctorListIntent.Search -> {
                fetchDoctorsJob?.cancel()
                fetchDoctorsJob = launchSearchDoctors(query = _uiState.value.searchQuery)
            }

            is DoctorListIntent.SelectDoctor -> {
                _uiState.update {
                    it.copy(effect = DoctorListEffect.NavigateToBookingRoute(doctor = intent.doctor))
                }
            }

            DoctorListIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun refreshDoctors() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listDoctorsUseCase(forceUpdate = true)
                .onEach { result ->
                    result
                        .onSuccess { doctors ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    searchQuery = "",
                                    doctors = doctors,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = DoctorListEffect.ShowError(message = error.toUiText()),
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
                fetchDoctorsJob?.cancel()
                fetchDoctorsJob = launchSearchDoctors(query)
            }.launchIn(viewModelScope)
    }

    fun launchSearchDoctors(query: String) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listDoctorsUseCase().collect { result ->
                result
                    .onSuccess { doctors ->
                        val queryResults =
                            doctors.filter { doctor ->
                                query.isEmpty() ||
                                    doctor.name.contains(query, ignoreCase = true) ||
                                    doctor.specialty.contains(query, ignoreCase = true)
                            }
                        _uiState.update { it.copy(isLoading = false, doctors = queryResults) }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = DoctorListEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }
}
