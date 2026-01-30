package eg.edu.cu.csds.icare.feature.appointment.screen.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetPatientAppointmentsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.UpdateAppointmentUseCase
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
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
class AppointmentListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getPatientAppointmentsUseCase: GetPatientAppointmentsUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
) : ViewModel() {
    private var getAppointmentsJob: Job? = null
    private var cancelAppointmentJob: Job? = null
    private val _uiState = MutableStateFlow(AppointmentListState())
    val uiState =
        _uiState
            .onStart {
                getAppointmentsJob = launchGetAppointments()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun handleIntent(intent: AppointmentListIntent) {
        when (intent) {
            is AppointmentListIntent.OnBackClick -> {
                _uiState.update { it.copy(effect = AppointmentListEffect.OnBackClick) }
            }

            is AppointmentListIntent.Refresh -> {
                getAppointmentsJob?.cancel()
                getAppointmentsJob = launchGetAppointments()
            }

            is AppointmentListIntent.RescheduleAppointment -> {
                _uiState.update {
                    it.copy(
                        effect =
                            AppointmentListEffect
                                .NavigateToRescheduleAppointment(intent.appointment),
                    )
                }
            }

            is AppointmentListIntent.CancelAppointment -> {
                cancelAppointmentJob?.cancel()
                cancelAppointmentJob =
                    launchCancelAppointment(
                        intent.appointment.copy(statusId = AppointmentStatus.CancelledStatus.code),
                    )
            }

            is AppointmentListIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun launchGetAppointments() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getPatientAppointmentsUseCase()
                .onEach { result ->
                    result
                        .onSuccess { appointments ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    appointments = appointments,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = AppointmentListEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun launchCancelAppointment(appointment: Appointment) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            updateAppointmentUseCase(appointment)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = AppointmentListEffect.ShowSuccess,
                                )
                            }
                            getAppointmentsJob?.cancel()
                            getAppointmentsJob = launchGetAppointments()
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = AppointmentListEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
