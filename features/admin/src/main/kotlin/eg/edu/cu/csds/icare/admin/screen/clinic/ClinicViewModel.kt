package eg.edu.cu.csds.icare.admin.screen.clinic

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.GetDoctorScheduleUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListTopDoctorsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ClinicViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listDoctorsUseCase: ListDoctorsUseCase,
    private val listTopDoctorsUseCase: ListTopDoctorsUseCase,
    private val getDoctorScheduleUseCase: GetDoctorScheduleUseCase,
) : ViewModel() {
    private val _doctorsResFlow = MutableStateFlow<Resource<List<Doctor>>>(Resource.Unspecified())
    val doctorsResFlow: StateFlow<Resource<List<Doctor>>> = _doctorsResFlow
    private val _topDoctorsResFlow =
        MutableStateFlow<Resource<List<Doctor>>>(Resource.Unspecified())
    val topDoctorsResFlow: StateFlow<Resource<List<Doctor>>> = _topDoctorsResFlow
    private val _doctorScheduleResFlow =
        MutableStateFlow<Resource<DoctorSchedule>>(Resource.Unspecified())
    val doctorScheduleResFlow: StateFlow<Resource<DoctorSchedule>> = _doctorScheduleResFlow
    var selectedDoctorState: MutableState<Doctor?> = mutableStateOf(null)

    var isRefreshing = mutableStateOf(false)
    var searchQueryState = mutableStateOf("")

    fun listDoctors(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            searchQueryState.value = ""
            if (_doctorsResFlow.value !is Resource.Unspecified) {
                _doctorsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listDoctorsUseCase(forceRefresh).collect { result ->
                result
                    .onSuccess { doctors ->
                        _doctorsResFlow.value = Resource.Success(doctors)
                    }.onError {
                        _doctorsResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }

    fun searchDoctors() {
        viewModelScope.launch(dispatcher) {
            if (_doctorsResFlow.value !is Resource.Unspecified) {
                _doctorsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listDoctorsUseCase().collect { result ->
                result
                    .onSuccess { doctors ->

                        val filtered =
                            doctors.filter { doctor ->
                                doctor.name.contains(
                                    searchQueryState.value,
                                    ignoreCase = true,
                                ) ||
                                    doctor.specialty.contains(
                                        searchQueryState.value,
                                        ignoreCase = true,
                                    )
                            }
                        _doctorsResFlow.value = Resource.Success(filtered)
                    }.onError {
                        _doctorsResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }

    fun listTopDoctors() {
        viewModelScope.launch(dispatcher) {
            searchQueryState.value = ""
            if (_topDoctorsResFlow.value !is Resource.Unspecified) {
                _topDoctorsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listTopDoctorsUseCase().collect { result ->
                result
                    .onSuccess {
                        _topDoctorsResFlow.value = Resource.Success(it)
                    }.onError {
                        _topDoctorsResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }

    fun getDoctorSchedule(doctorId: String) {
        viewModelScope.launch(dispatcher) {
            if (_doctorScheduleResFlow.value !is Resource.Unspecified) {
                _doctorScheduleResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            getDoctorScheduleUseCase(doctorId).collect { result ->
                result
                    .onSuccess { schedule ->
                        _doctorScheduleResFlow.value = Resource.Success(schedule)
                    }.onError {
                        _doctorScheduleResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }
}
