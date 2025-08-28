package eg.edu.cu.csds.icare.home.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAdminStatisticsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAppointmentsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.UpdateAppointmentUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.GetCurrentDoctorUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.GetDoctorScheduleUseCase
import eg.edu.cu.csds.icare.core.ui.common.Role
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.home.screen.home.HomeEffect.NavigateToRoute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getAdminStatisticsUseCase: GetAdminStatisticsUseCase,
    private val getCurrentDoctorUseCase: GetCurrentDoctorUseCase,
    private val getDoctorScheduleUseCase: GetDoctorScheduleUseCase,
    private val getAppointmentUseCase: GetAppointmentsUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
) : ViewModel() {
    private var getCurrentUserJob: Job? = null
    private var getAdminStatisticsJob: Job? = null
    private var getCurrentDoctorJob: Job? = null
    private var getDoctorScheduleJob: Job? = null
    private var getAppointmentsJob: Job? = null
    private var confirmAppointmentJob: Job? = null
    private val _uiState = MutableStateFlow(HomeState())
    val uiState =
        _uiState
            .onStart {
                getCurrentUserJob = launchGetCurrentUser()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = _uiState.map { it.effect }

    fun processEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.NavigateToProfileScreen ->
                _uiState.update {
                    it.copy(effect = NavigateToRoute(route = Route.Profile))
                }

            is HomeEvent.Refresh -> {
                getCurrentUserJob?.cancel()
                getCurrentUserJob = launchGetCurrentUser(true)
            }

            is HomeEvent.UpdateOpenDialog ->
                _uiState.update { it.copy(openDialog = event.isOpen) }

            is HomeEvent.NavigateToAppointmentsScreen -> Unit

            is HomeEvent.ConfirmAppointment -> {
                confirmAppointmentJob?.cancel()
                confirmAppointmentJob = launchConfirmAppointment(event.appointment)
            }

            is HomeEvent.NavigateToUpdateDoctorScreen ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToUpdateDoctorScreen(doctor = event.doctor))
                }

            is HomeEvent.NavigateToNewConsultation ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToNewConsultation(appointment = event.appointment))
                }

            is HomeEvent.NavigateToSectionsAdminScreen ->
                _uiState.update {
                    it.copy(effect = NavigateToRoute(route = Route.Admin))
                }

            is HomeEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchGetCurrentUser(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase(forceUpdate = forceUpdate).collect { result ->
                result
                    .onSuccess { user ->
                        _uiState.update { it.copy(isLoading = false, currentUser = user) }
                        when (user.roleId) {
                            Role.AdminRole.code -> {
                                getAdminStatisticsJob?.cancel()
                                getAdminStatisticsJob = launchGetAdminStatistics()
                            }

                            Role.DoctorRole.code -> {
                                getCurrentDoctorJob?.cancel()
                                getCurrentDoctorJob = launchGetCurrentDoctor()
                            }

                            Role.ClinicianRole.code -> {
                                getAppointmentsJob?.cancel()
                                getAppointmentsJob = launchGetAppointments()
                            }

                            Role.PharmacistRole.code -> {
                                Unit
                            }

                            Role.CenterStaffRole.code -> {
                                Unit
                            }
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(effect = HomeEffect.ShowError(message = error.toUiText()))
                        }
                    }
            }
        }

    private fun launchGetAdminStatistics() =
        viewModelScope.launch(dispatcher) {
            getAdminStatisticsUseCase().collect { result ->
                _uiState.update { it.copy(isLoading = true) }
                result
                    .onSuccess { adminStatistics ->
                        _uiState.update {
                            it.copy(isLoading = false, adminStatistics = adminStatistics)
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = HomeEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }

    private fun launchGetCurrentDoctor() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getCurrentDoctorUseCase().collect { result ->
                result
                    .onSuccess { doctor ->
                        _uiState.update { it.copy(isLoading = false, currentDoctor = doctor) }
                        getDoctorScheduleJob?.cancel()
                        getDoctorScheduleJob = launchGetDoctorSchedule()
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = HomeEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }

    private fun launchGetDoctorSchedule() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getDoctorScheduleUseCase().collect { result ->
                result
                    .onSuccess { schedule ->
                        _uiState.update { it.copy(isLoading = false, doctorSchedule = schedule) }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = HomeEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }

    private fun launchGetAppointments() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }

            getAppointmentUseCase().collectLatest { result ->
                result
                    .onSuccess { appointments ->
                        _uiState.update { it.copy(isLoading = false, appointments = appointments) }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = HomeEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }

    private fun launchConfirmAppointment(appointment: Appointment) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            updateAppointmentUseCase(appointment).collect { result ->
                result
                    .onSuccess {
                        launchGetAppointments()
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = HomeEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }
}
