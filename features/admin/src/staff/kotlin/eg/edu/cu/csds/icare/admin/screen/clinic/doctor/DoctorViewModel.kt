package eg.edu.cu.csds.icare.admin.screen.clinic.doctor

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.AddNewDoctor
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctors
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.UpdateDoctor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DoctorViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewDoctorUseCase: AddNewDoctor,
    private val updateDoctorUseCase: UpdateDoctor,
    private val listDoctorsUseCase: ListDoctors,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: StateFlow<Resource<Nothing?>> = _actionResFlow
    private val _doctorsResFlow =
        MutableStateFlow<Resource<List<Doctor>>>(Resource.Unspecified())
    val doctorsResFlow: StateFlow<Resource<List<Doctor>>> = _doctorsResFlow

    var idState = mutableLongStateOf(0)
        private set
    var firstNameState = mutableStateOf("")
        private set
    var lastNameState = mutableStateOf("")
        private set
    var clinicIdState = mutableLongStateOf(0)
        private set
    var emailState = mutableStateOf("")
        private set
    var phoneState = mutableStateOf("")
        private set
    var specialityState = mutableStateOf("")
        private set
    var fromTimeState = mutableLongStateOf(0)
        private set
    var toTimeState = mutableLongStateOf(0)
        private set

    fun addNewDoctor() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewDoctorUseCase(
                Doctor(
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    clinicId = clinicIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    specialty = specialityState.value,
                    fromTime = fromTimeState.longValue,
                    toTime = toTimeState.longValue,
                    profilePicture = "",
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }
    }

    fun updateDoctor() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            updateDoctorUseCase(
                Doctor(
                    id = idState.longValue,
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    clinicId = clinicIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    specialty = specialityState.value,
                    fromTime = fromTimeState.longValue,
                    toTime = toTimeState.longValue,
                    profilePicture = "",
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }
    }

    fun listDoctors() {
        viewModelScope.launch(dispatcher) {
            if (_doctorsResFlow.value !is Resource.Unspecified) {
                _doctorsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listDoctorsUseCase().collect { result ->
                _doctorsResFlow.value = result
            }
        }
    }
}
