package eg.edu.cu.csds.icare.feature.admin.screen.clinician.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.ListCliniciansUseCase
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
class ClinicianListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listCliniciansUseCase: ListCliniciansUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClinicianListState())
    val uiState =
        _uiState
            .onStart {
                fetchClinicians()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = _uiState.map { it.effect }

    fun processEvent(event: ClinicianListEvent) {
        when (event) {
            is ClinicianListEvent.Refresh -> {
                fetchClinicians()
            }

            is ClinicianListEvent.SelectClinician ->
                _uiState.update {
                    it.copy(
                        effect =
                            ClinicianListEffect.NavigateToClinicianDetails(event.clinician),
                    )
                }

            is ClinicianListEvent.UpdateFabExpanded ->
                _uiState.update {
                    it.copy(
                        effect =
                            ClinicianListEffect
                                .UpdateFabExpanded(isExpanded = event.isExpanded),
                    )
                }
            ClinicianListEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun fetchClinicians() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCliniciansUseCase()
                .onEach { result ->
                    result
                        .onSuccess { clinicians ->
                            _uiState.update { it.copy(isLoading = false, clinicians = clinicians) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ClinicianListEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
