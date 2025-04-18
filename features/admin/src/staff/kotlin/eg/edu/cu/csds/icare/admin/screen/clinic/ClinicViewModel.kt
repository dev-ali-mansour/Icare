package eg.edu.cu.csds.icare.admin.screen.clinic

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.AddNewClinic
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinics
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.UpdateClinic
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.AddNewClinicStaff
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.ListClinicStaff
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff.UpdateClinicStaff
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.AddNewDoctor
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctors
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.UpdateDoctor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ClinicViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewClinicUseCase: AddNewClinic,
    private val updateClinicUseCase: UpdateClinic,
    private val listClinicsUseCase: ListClinics,
    private val addNewDoctorUseCase: AddNewDoctor,
    private val updateDoctorUseCase: UpdateDoctor,
    private val listDoctorsUseCase: ListDoctors,
    private val addNewClinicStaffUseCase: AddNewClinicStaff,
    private val updateClinicStaffUseCase: UpdateClinicStaff,
    private val listClinicStaffUseCase: ListClinicStaff,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: SharedFlow<Resource<Nothing?>> =
        _actionResFlow.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 0,
        )
    private val _clinicsResFlow = MutableStateFlow<Resource<List<Clinic>>>(Resource.Unspecified())
    val clinicsResFlow: StateFlow<Resource<List<Clinic>>> = _clinicsResFlow
    private val _doctorsResFlow = MutableStateFlow<Resource<List<Doctor>>>(Resource.Unspecified())
    val doctorsResFlow: StateFlow<Resource<List<Doctor>>> = _doctorsResFlow
    private val _clinicStaffsResFlow = MutableStateFlow<Resource<List<ClinicStaff>>>(Resource.Unspecified())
    val clinicStaffsResFlow: StateFlow<Resource<List<ClinicStaff>>> = _clinicStaffsResFlow
    var selectedClinicState: MutableState<Clinic?> = mutableStateOf(null)
    var selectedDoctorState: MutableState<Doctor?> = mutableStateOf(null)
    var selectedClinicStaffState: MutableState<ClinicStaff?> = mutableStateOf(null)

    var selectedClinicIdState = mutableLongStateOf(0)
    var idState = mutableLongStateOf(0)
    var nameState = mutableStateOf("")
    var typeState = mutableStateOf("")
    var phoneState = mutableStateOf("")
    var addressState = mutableStateOf("")
    var longitudeState = mutableDoubleStateOf(0.0)
    var latitudeState = mutableDoubleStateOf(0.0)
    var isOpenState = mutableStateOf(false)
    var firstNameState = mutableStateOf("")
    var lastNameState = mutableStateOf("")
    var clinicIdState = mutableLongStateOf(0)
    var emailState = mutableStateOf("")
    var specialityState = mutableStateOf("")
    var fromTimeState = mutableLongStateOf(0)
    var toTimeState = mutableLongStateOf(0)
    var profilePictureState = mutableStateOf("")
    var expandedFab = mutableStateOf(true)

    fun addNewClinic() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewClinicUseCase(
                Clinic(
                    name = nameState.value,
                    type = typeState.value,
                    address = addressState.value,
                    phone = phoneState.value,
                    longitude = longitudeState.doubleValue,
                    latitude = latitudeState.doubleValue,
                    isOpen = isOpenState.value,
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }
    }

    fun updateClinic() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            updateClinicUseCase(
                Clinic(
                    id = idState.longValue,
                    name = nameState.value,
                    type = typeState.value,
                    address = addressState.value,
                    phone = phoneState.value,
                    longitude = longitudeState.doubleValue,
                    latitude = latitudeState.doubleValue,
                    isOpen = isOpenState.value,
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }
    }

    fun listClinics() {
        viewModelScope.launch(dispatcher) {
            if (_clinicsResFlow.value !is Resource.Unspecified) {
                _clinicsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listClinicsUseCase().collect { result ->
                _clinicsResFlow.value = result
            }
        }
    }

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
                    profilePicture = profilePictureState.value,
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
                    profilePicture = profilePictureState.value,
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

    fun addNewStaff() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewClinicStaffUseCase(
                ClinicStaff(
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    clinicId = clinicIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    profilePicture = profilePictureState.value,
                ),
            ).collect { result ->
                _actionResFlow.emit(result)
            }
        }
    }

    fun updateStaff() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            updateClinicStaffUseCase(
                ClinicStaff(
                    id = idState.longValue,
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    clinicId = clinicIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    profilePicture = profilePictureState.value,
                ),
            ).collect { result ->
                _actionResFlow.emit(result)
            }
        }
    }

    fun listStaffs() {
        viewModelScope.launch(dispatcher) {
            if (_clinicStaffsResFlow.value !is Resource.Unspecified) {
                _clinicStaffsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listClinicStaffUseCase(selectedClinicIdState.longValue).collect { result ->
                _clinicStaffsResFlow.value = result
            }
        }
    }
}
