package eg.edu.cu.csds.icare.feature.admin.screen.clinic.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
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
class ClinicListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listClinicsUseCase: ListClinicsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClinicListState())
    val uiState =
        _uiState
            .onStart {
                fetchClinics()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun handleIntent(intent: ClinicListIntent) {
        when (intent) {
            is ClinicListIntent.Refresh -> {
                fetchClinics(forceUpdate = true)
            }

            is ClinicListIntent.SelectClinic -> {
                _uiState.update {
                    it.copy(effect = ClinicListEffect.NavigateToClinicDetails(clinic = intent.clinic))
                }
            }

            is ClinicListIntent.UpdateFabExpanded -> {
                _uiState.update {
                    it.copy(effect = ClinicListEffect.UpdateFabExpanded(isExpanded = intent.isExpanded))
                }
            }

            is ClinicListIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun fetchClinics(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listClinicsUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { clinics ->
                            _uiState.update { it.copy(isLoading = false, clinics = clinics) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ClinicListEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
