package eg.edu.cu.csds.icare.consultation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.GetMedicalRecordUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MedicalRecordViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getMedicalRecordUseCase: GetMedicalRecordUseCase,
) : ViewModel() {
    private var getMedicalRecordJob: Job? = null
    private val _uiState = MutableStateFlow(MedicalRecordState())
    val uiState =
        _uiState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )

    val effect = uiState.map { it.effect }

    fun processEvent(event: MedicalRecordEvent) {
        when (event) {
            is MedicalRecordEvent.NavigateToConsultation ->
                _uiState.update {
                    it.copy(effect = MedicalRecordEffect.NavigateToConsultation(event.consultation))
                }

            is MedicalRecordEvent.LoadMedicalRecord -> {
                getMedicalRecordJob?.cancel()
                getMedicalRecordJob = launchGetMedicalRecord(event.patientId)
            }

            is MedicalRecordEvent.Refresh -> {
                _uiState.value.medicalRecord?.patientId?.let { patientId ->
                    getMedicalRecordJob?.cancel()
                    getMedicalRecordJob = launchGetMedicalRecord(patientId)
                }
            }

            MedicalRecordEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchGetMedicalRecord(patientId: String) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getMedicalRecordUseCase(patientId)
                .onEach { result ->
                    result
                        .onSuccess { medicalRecord ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    medicalRecord = medicalRecord,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = MedicalRecordEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
