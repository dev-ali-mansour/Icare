package eg.edu.cu.csds.icare.consultation.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.consultation.screen.ConsultationEffect
import eg.edu.cu.csds.icare.consultation.screen.ConsultationEvent
import eg.edu.cu.csds.icare.consultation.screen.ConsultationState
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.AddNewConsultationUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
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
class NewConsultationViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
    private val listCentersUseCase: ListCentersUseCase,
    private val addNewConsultationUseCase: AddNewConsultationUseCase,
) : ViewModel() {
    private var addConsultationJob: Job? = null
    private val _uiState = MutableStateFlow(ConsultationState())
    val uiState =
        _uiState
            .onStart {
                _uiState.update { it.copy(readOnly = false) }
                fetchPharmacies()
                fetchCenters()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )

    val effect = uiState.map { it.effect }

    fun processEvent(event: ConsultationEvent) {
        when (event) {
            is ConsultationEvent.NavigateToMedicalRecord ->
                _uiState.update {
                    it.copy(effect = ConsultationEffect.NavigateToMedicalRecord(event.patientId))
                }

            is ConsultationEvent.Refresh -> {
                fetchPharmacies(forceUpdate = true)
                fetchCenters(forceUpdate = true)
            }

            is ConsultationEvent.UpdateAppointment ->
                _uiState.update { it.copy(appointment = event.appointment) }

            is ConsultationEvent.UpdateDiagnosis ->
                _uiState.update { it.copy(diagnosis = event.diagnosis) }
            is ConsultationEvent.UpdatePharmacyId ->
                _uiState.update { it.copy(pharmacyId = event.pharmacyId) }
            is ConsultationEvent.UpdatePharmaciesExpanded ->
                _uiState.update { it.copy(isPharmaciesExpanded = event.isExpanded) }
            is ConsultationEvent.UpdateMedications ->
                _uiState.update { it.copy(medications = event.medications) }
            is ConsultationEvent.UpdateLabCenterId ->
                _uiState.update { it.copy(labCenterId = event.labCenterId) }
            is ConsultationEvent.UpdateLabCentersExpanded ->
                _uiState.update { it.copy(isLabCentersExpanded = event.isExpanded) }
            is ConsultationEvent.UpdateLabTests ->
                _uiState.update { it.copy(labTests = event.labTests) }
            is ConsultationEvent.UpdateImagingCenterId ->
                _uiState.update { it.copy(imagingCenterId = event.imagingCenterId) }
            is ConsultationEvent.UpdateImagingCentersExpanded ->
                _uiState.update { it.copy(isImagingCentersExpanded = event.isExpanded) }
            is ConsultationEvent.UpdateImagingTests ->
                _uiState.update { it.copy(imagingTests = event.imagingTests) }
            is ConsultationEvent.UpdateFollowUpdDate ->
                _uiState.update { it.copy(followUpdDate = event.followUpdDate) }

            is ConsultationEvent.LoadConsultation -> Unit

            is ConsultationEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.diagnosis
                            .trim()
                            .isEmpty() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.diagnosis_error),
                                        ),
                                )
                            }
                        }

                        _uiState.value.pharmacyId == 0.toLong() &&
                            _uiState.value.medications
                                .trim()
                                .isNotEmpty()
                        -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.error_pharmacy),
                                        ),
                                )
                            }
                        }

                        _uiState.value.pharmacyId > 0.toLong() &&
                            _uiState.value.medications
                                .trim()
                                .isEmpty()
                        -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.medications_error),
                                        ),
                                )
                            }
                        }

                        _uiState.value.labCenterId != 0.toLong() &&
                            _uiState.value.labTests
                                .trim()
                                .isNotEmpty()
                        -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.lab_center_error),
                                        ),
                                )
                            }
                        }

                        _uiState.value.labCenterId > 0.toLong() &&
                            _uiState.value.labTests
                                .trim()
                                .isEmpty()
                        -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.lab_tests_error),
                                        ),
                                )
                            }
                        }

                        _uiState.value.imagingCenterId == 0.toLong() &&
                            _uiState.value.imagingTests
                                .trim()
                                .isNotEmpty() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.imaging_center_error),
                                        ),
                                )
                            }
                        }

                        _uiState.value.imagingCenterId > 0.toLong() &&
                            _uiState.value.imagingTests
                                .trim()
                                .isEmpty() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.imaging_tests_error),
                                        ),
                                )
                            }
                        }

                        _uiState.value.followUpdDate == 0L -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        ConsultationEffect.ShowError(
                                            message = StringResourceId(R.string.follow_up_date_error),
                                        ),
                                )
                            }
                        }

                        else -> {
                            addConsultationJob?.cancel()
                            addConsultationJob = launchAddNewConsultation()
                        }
                    }
                }

            ConsultationEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchAddNewConsultation() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val consultation =
                Consultation(
                    appointment = _uiState.value.appointment,
                    dateTime = System.currentTimeMillis(),
                    diagnosis = _uiState.value.diagnosis,
                    pharmacyId = _uiState.value.pharmacyId,
                    medications = _uiState.value.medications,
                    labCenterId = _uiState.value.labCenterId,
                    labTests = _uiState.value.labTests,
                    imagingCenterId = _uiState.value.imagingCenterId,
                    imagingTests = _uiState.value.imagingTests,
                    followUpdDate = _uiState.value.followUpdDate,
                )
            addNewConsultationUseCase(consultation)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ConsultationEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ConsultationEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun fetchPharmacies(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listPharmaciesUseCase(forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { pharmacies ->
                            _uiState.update { it.copy(pharmacies = pharmacies, isLoading = false) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ConsultationEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun fetchCenters(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCentersUseCase(forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { centers ->
                            _uiState.update { it.copy(isLoading = false, centers = centers) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ConsultationEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
