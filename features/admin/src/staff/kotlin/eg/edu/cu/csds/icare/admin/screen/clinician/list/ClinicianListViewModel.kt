package eg.edu.cu.csds.icare.admin.screen.clinician.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.ListCliniciansUseCase
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
class ClinicianListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listCliniciansUseCase: ListCliniciansUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ClinicianListState())
    val state =
        _state
            .onStart {
                fetchClinicians()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<ClinicianListSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: ClinicianListIntent) {
        when (intent) {
            is ClinicianListIntent.Refresh -> {
                fetchClinicians()
            }

            is ClinicianListIntent.SelectClinician -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        ClinicianListSingleEvent
                            .NavigateToClinicianDetails(clinician = intent.clinician),
                    )
                }
            }

            is ClinicianListIntent.UpdateFabExpanded -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        ClinicianListSingleEvent
                            .UpdateFabExpanded(isExpanded = intent.isExpanded),
                    )
                }
            }
        }
    }

    private fun fetchClinicians() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            listCliniciansUseCase()
                .onEach { result ->
                    result
                        .onSuccess { clinicians ->
                            _state.update { it.copy(clinicians = clinicians, isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(ClinicianListSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
