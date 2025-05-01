package eg.edu.cu.csds.icare.appointment

import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.BookAppointment
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAdminStatistics
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAppointments
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetAppointmentsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetPatientAppointments
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.UpdateAppointment
import eg.edu.cu.csds.icare.core.ui.common.AppointmentStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AppointmentViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val bookAppointmentUseCase: BookAppointment,
    private val updateAppointmentUseCase: UpdateAppointment,
    private val getAppointmentsByStatusUseCase: GetAppointmentsByStatus,
    private val getPatientAppointmentUseCase: GetPatientAppointments,
    private val getAdminStatisticsUseCase: GetAdminStatistics,
    private val getAppointmentUseCase: GetAppointments,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: SharedFlow<Resource<Nothing?>> =
        _actionResFlow.shareIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 0,
        )

    private val _appointmentsResFlow =
        MutableStateFlow<Resource<List<Appointment>>>(Resource.Unspecified())
    val appointmentsResFlow: StateFlow<Resource<List<Appointment>>> =
        _appointmentsResFlow.asStateFlow()
    private val _adminStatsRes = MutableStateFlow<Resource<AdminStatistics>>(Resource.Unspecified())
    val adminStatsRes: StateFlow<Resource<AdminStatistics>> = _adminStatsRes.asStateFlow()
    var selectedAppointmentState: MutableState<Appointment?> = mutableStateOf(null)
    var selectedStatusIdState: MutableState<Short> = mutableStateOf(1)
    var selectedSlotState: MutableLongState = mutableLongStateOf(0)
    val statusListState =
        mutableStateOf(
            listOf(
                AppointmentStatus.PendingStatus,
                AppointmentStatus.ConfirmedStatus,
                AppointmentStatus.CancelledStatus,
                AppointmentStatus.CompletedStatus,
            ),
        )

    var doctorIdState = mutableStateOf("")
    var patientIdState = mutableStateOf("")
    var isRefreshing = mutableStateOf(false)

    fun bookAppointment() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            bookAppointmentUseCase(
                Appointment(
                    patientId = patientIdState.value,
                    doctorId = doctorIdState.value,
                    dateTime = selectedSlotState.value,
                    statusId = 1,
                ),
            ).collectLatest {
                _actionResFlow.value = it
            }
        }
    }

    fun updateAppointment() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            selectedAppointmentState.value?.let {
                updateAppointmentUseCase(it).collect { result ->
                    if (result is Resource.Success) {
                        selectedAppointmentState.value = null
                        getPatientAppointments()
                    }
                    _actionResFlow.emit(result)
                }
            } ?: run {
                _actionResFlow.emit(Resource.Error(Error("No appointment selected!")))
            }
        }
    }

    fun getAppointmentsByStatus() {
        viewModelScope.launch(dispatcher) {
            if (_appointmentsResFlow.value !is Resource.Unspecified) {
                _appointmentsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getAppointmentsByStatusUseCase(selectedStatusIdState.value).collectLatest {
                _appointmentsResFlow.value = it
            }
        }
    }

    fun getPatientAppointments() {
        viewModelScope.launch(dispatcher) {
            if (_appointmentsResFlow.value !is Resource.Unspecified) {
                _appointmentsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getPatientAppointmentUseCase().collectLatest {
                _appointmentsResFlow.value = it
            }
        }
    }

    fun getAdminStatistics() {
        viewModelScope.launch(dispatcher) {
            if (_adminStatsRes.value !is Resource.Unspecified) {
                _adminStatsRes.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getAdminStatisticsUseCase().collect {
                _adminStatsRes.value = it
            }
        }
    }

    fun getAppointments() {
        viewModelScope.launch(dispatcher) {
            if (_appointmentsResFlow.value !is Resource.Unspecified) {
                _appointmentsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            getAppointmentUseCase().collectLatest {
                _appointmentsResFlow.value = it
            }
        }
    }
}
