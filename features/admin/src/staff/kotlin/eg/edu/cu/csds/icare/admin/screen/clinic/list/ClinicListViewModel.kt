package eg.edu.cu.csds.icare.admin.screen.clinic.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ClinicListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listClinicsUseCase: ListClinicsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ClinicListState())
    val state =
        _state
            .onStart {
                fetchClinics()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<ClinicListSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: ClinicListIntent) {
        when (intent) {
            is ClinicListIntent.Refresh -> {
                fetchClinics(forceUpdate = true)
            }

            is ClinicListIntent.SelectClinic -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        ClinicListSingleEvent
                            .NavigateToClinicDetails(clinic = intent.clinic),
                    )
                }
            }

            is ClinicListIntent.UpdateFabExpanded -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        ClinicListSingleEvent
                            .UpdateFabExpanded(isExpanded = intent.isExpanded),
                    )
                }
            }
        }
    }

    private fun fetchClinics(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            listClinicsUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { clinics ->
                            _state.update { it.copy(clinics = clinics, isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(ClinicListSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
