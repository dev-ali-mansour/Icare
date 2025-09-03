package eg.edu.cu.csds.icare.appointment.screen.reschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.UpdateAppointmentUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.GetDoctorScheduleUseCase
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
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
class RescheduleViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getDoctorScheduleUseCase: GetDoctorScheduleUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
) : ViewModel() {
    private var getDoctorScheduleJob: Job? = null
    private var rescheduleJob: Job? = null
    private val _uiState = MutableStateFlow(RescheduleState())
    val uiState =
        _uiState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(event: RescheduleEvent) {
        when (event) {
            is RescheduleEvent.Refresh -> {
                _uiState.value.appointment?.let { appointment ->
                    getDoctorScheduleJob?.cancel()
                    getDoctorScheduleJob = launchGetDoctorSchedule(appointment.doctorId)
                }
            }

            is RescheduleEvent.SelectAppointment -> {
                _uiState.update {
                    it.copy(appointment = event.appointment)
                }
                _uiState.value.appointment?.let { appointment ->
                    getDoctorScheduleJob?.cancel()
                    getDoctorScheduleJob = launchGetDoctorSchedule(appointment.doctorId)
                }
            }

            is RescheduleEvent.UpdateSelectedSlot -> {
                _uiState.update { it.copy(appointment = it.appointment?.copy(dateTime = event.slot)) }
            }

            is RescheduleEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.appointment == null ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        RescheduleEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string
                                                        .feature_appointments_error_no_appointment_selected,
                                                ),
                                        ),
                                )
                            }

                        else -> {
                            rescheduleJob?.cancel()
                            rescheduleJob = launchRescheduleAppointment()
                        }
                    }
                }

            RescheduleEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchGetDoctorSchedule(uid: String) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getDoctorScheduleUseCase(uid).collect { result ->
                result
                    .onSuccess { schedule ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                doctorSchedule = schedule,
                            )
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = RescheduleEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }

    private fun launchRescheduleAppointment() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.value.appointment?.let { appointment ->
                updateAppointmentUseCase(appointment)
                    .onEach { result ->
                        result
                            .onSuccess { _ ->
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        effect = RescheduleEffect.ShowSuccess,
                                    )
                                }
                            }.onError { error ->
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        effect = RescheduleEffect.ShowError(message = error.toUiText()),
                                    )
                                }
                            }
                    }.launchIn(viewModelScope)
            }
        }
}
