package eg.edu.cu.csds.icare.feature.consultation.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.consultation.AddNewConsultationUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.feature.admin.R
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationEffect
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationIntent
import eg.edu.cu.csds.icare.feature.consultation.screen.ConsultationState
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
                _uiState.update { it.copy(dateTime = System.currentTimeMillis(), readOnly = false) }
                fetchPharmacies()
                fetchCenters()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )

    val effect = uiState.map { it.effect }

    fun handleIntent(event: ConsultationIntent) {
        when (event) {
            is ConsultationIntent.NavigateToMedicalRecord -> {
                _uiState.update {
                    it.copy(effect = ConsultationEffect.NavigateToMedicalRecord(event.patientId))
                }
            }

            is ConsultationIntent.Refresh -> {
                fetchPharmacies(forceUpdate = true)
                fetchCenters(forceUpdate = true)
            }

            is ConsultationIntent.UpdateAppointment -> {
                _uiState.update { it.copy(appointment = event.appointment) }
            }

            is ConsultationIntent.UpdateDiagnosis -> {
                _uiState.update { it.copy(diagnosis = event.diagnosis) }
            }

            is ConsultationIntent.UpdatePharmacyId -> {
                _uiState.update { it.copy(pharmacyId = event.pharmacyId) }
            }

            is ConsultationIntent.UpdatePharmaciesExpanded -> {
                _uiState.update { it.copy(isPharmaciesExpanded = event.isExpanded) }
            }

            is ConsultationIntent.UpdateMedications -> {
                _uiState.update { it.copy(medications = event.medications) }
            }

            is ConsultationIntent.UpdateLabCenterId -> {
                _uiState.update { it.copy(labCenterId = event.labCenterId) }
            }

            is ConsultationIntent.UpdateLabCentersExpanded -> {
                _uiState.update { it.copy(isLabCentersExpanded = event.isExpanded) }
            }

            is ConsultationIntent.UpdateLabTests -> {
                _uiState.update { it.copy(labTests = event.labTests) }
            }

            is ConsultationIntent.UpdateImagingCenterId -> {
                _uiState.update { it.copy(imagingCenterId = event.imagingCenterId) }
            }

            is ConsultationIntent.UpdateImagingCentersExpanded -> {
                _uiState.update { it.copy(isImagingCentersExpanded = event.isExpanded) }
            }

            is ConsultationIntent.UpdateImagingTests -> {
                _uiState.update { it.copy(imagingTests = event.imagingTests) }
            }

            is ConsultationIntent.UpdateFollowUpdDate -> {
                _uiState.update { it.copy(followUpdDate = event.followUpdDate) }
            }

            is ConsultationIntent.LoadConsultation -> {
                Unit
            }

            is ConsultationIntent.Proceed -> {
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin__error_diagnosis,
                                                ),
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_pharmacy,
                                                ),
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin__error_medications,
                                                ),
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin__error_lab_center,
                                                ),
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin__error_lab_tests,
                                                ),
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin__error_imaging_center,
                                                ),
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin__error_imaging_tests,
                                                ),
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
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin__error_follow_up_date,
                                                ),
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
            }

            ConsultationIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
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
