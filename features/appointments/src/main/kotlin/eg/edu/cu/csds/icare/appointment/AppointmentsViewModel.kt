package eg.edu.cu.csds.icare.appointment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.BookAppointment
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.GetAppointmentsByStatus
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.GetPatientAppointments
import eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment.UpdateAppointment
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
class AppointmentsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val bookAppointmentUseCase: BookAppointment,
    private val updateAppointmentUseCase: UpdateAppointment,
    private val getAppointmentsByStatusUseCase: GetAppointmentsByStatus,
    private val getPatientAppointmentUseCase: GetPatientAppointments,
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
    var selectedAppointment: MutableState<Appointment?> = mutableStateOf(null)
    var selectedStatusIdState: MutableState<Short> = mutableStateOf(1)
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
    var doctorNameState = mutableStateOf("")
    var doctorSpecialtyState = mutableStateOf("")
    var doctorImageState = mutableStateOf("")
    var dateTimeState = mutableLongStateOf(0L)
    var patientNameState = mutableStateOf("")
    var patientImageState = mutableStateOf("")
    var statusIdState = mutableIntStateOf(0)
    var statusState = mutableStateOf("")

    fun bookAppointment() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            bookAppointmentUseCase(
                Appointment(
                    doctorName = doctorNameState.value,
                    doctorSpecialty = doctorSpecialtyState.value,
                    doctorId = doctorIdState.value,
                    doctorImage = doctorImageState.value,
                    dateTime = dateTimeState.longValue,
                    patientName = patientNameState.value,
                    patientImage = patientImageState.value,
                    statusId = statusIdState.intValue.toShort(),
                    status = statusState.value,
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

            selectedAppointment.value?.let {
                updateAppointmentUseCase(it).collect { result ->
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

            /*_appointmentsResFlow.value =
                Resource.Success(
                    listOf(
                        Appointment(
                            appointmentId = 1,
                            doctorName = "د.سيد عبدالحليم الجوهري",
                            doctorSpecialty = "استشاري جراحة الفم والأسنان",
                            doctorId = "101",
                            doctorImage = "",
                            dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                            statusId = AppointmentStatus.PendingStatus.code,
                        ),
                        Appointment(
                            appointmentId = 2,
                            doctorName = "د.محمد عبدالرحمن بدوي",
                            doctorSpecialty = "مخ وأعصاب",
                            doctorId = "102",
                            doctorImage = "",
                            dateTime = System.currentTimeMillis() + Constants.THREE_DAYS,
                            statusId = AppointmentStatus.PendingStatus.code,
                        ),
                        Appointment(
                            appointmentId = 3,
                            doctorName = "د.سارة محمد الهلالي",
                            doctorSpecialty = "نساء وتوليد",
                            doctorId = "101",
                            doctorImage = "",
                            dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                            statusId = AppointmentStatus.PendingStatus.code,
                        ),
                        Appointment(
                            appointmentId = 4,
                            doctorName = "Dr. Anna Jones",
                            doctorSpecialty = "Dentist",
                            doctorId = "101",
                            doctorImage = "",
                            dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                            patientName = "Patient",
                            patientImage = "",
                            statusId = AppointmentStatus.ConfirmedStatus.code,
                            status = context.getString(AppointmentStatus.ConfirmedStatus.textResId),
                        ),
                        Appointment(
                            appointmentId = 5,
                            doctorName = "Dr. Anna Jones",
                            doctorSpecialty = "General Practitioner",
                            doctorId = "101",
                            doctorImage = "",
                            dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                            patientName = "Patient",
                            patientImage = "",
                            statusId = AppointmentStatus.CompletedStatus.code,
                            status = context.getString(AppointmentStatus.CompletedStatus.textResId),
                        ),
                        Appointment(
                            appointmentId = 6,
                            doctorName = "Dr. Anna Jones",
                            doctorSpecialty = "General Practitioner",
                            doctorId = "101",
                            doctorImage = "",
                            dateTime = System.currentTimeMillis() + Constants.ONE_DAY,
                            patientName = "Patient",
                            patientImage = "",
                            statusId = AppointmentStatus.CancelledStatus.code,
                            status = context.getString(AppointmentStatus.CancelledStatus.textResId),
                        ),
                    ),
                )*/
            getPatientAppointmentUseCase().collectLatest {
                _appointmentsResFlow.value = it
            }
        }
    }
}
