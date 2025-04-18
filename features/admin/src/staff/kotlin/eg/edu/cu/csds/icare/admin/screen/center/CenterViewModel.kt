package eg.edu.cu.csds.icare.admin.screen.center

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.center.AddNewCenter
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCenters
import eg.edu.cu.csds.icare.core.domain.usecase.center.UpdateCenter
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.AddNewCenterStaff
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.ListCenterStaff
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.UpdateCenterStaff
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
class CenterViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewCenterUseCase: AddNewCenter,
    private val updateCenterUseCase: UpdateCenter,
    private val listCentersUseCase: ListCenters,
    private val addNewCenterStaffUseCase: AddNewCenterStaff,
    private val updateCenterStaffUseCase: UpdateCenterStaff,
    private val listCenterStaffUseCase: ListCenterStaff,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: SharedFlow<Resource<Nothing?>> =
        _actionResFlow.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 0,
        )
    private val _centersResFlow = MutableStateFlow<Resource<List<LabImagingCenter>>>(Resource.Unspecified())
    val centersResFlow: StateFlow<Resource<List<LabImagingCenter>>> = _centersResFlow
    private val _centerStaffsResFlow = MutableStateFlow<Resource<List<CenterStaff>>>(Resource.Unspecified())
    val centerStaffsResFlow: StateFlow<Resource<List<CenterStaff>>> = _centerStaffsResFlow
    var selectedCenterState: MutableState<LabImagingCenter?> = mutableStateOf(null)
    var selectedCenterStaffState: MutableState<CenterStaff?> = mutableStateOf(null)

    var selectedCenterIdState = mutableLongStateOf(0)
    var nameState = mutableStateOf("")
    var typeState = mutableStateOf(0.toShort())
    var phoneState = mutableStateOf("")
    var addressState = mutableStateOf("")
    var firstNameState = mutableStateOf("")
    var lastNameState = mutableStateOf("")
    var centerIdState = mutableLongStateOf(0)
    var emailState = mutableStateOf("")
    var profilePictureState = mutableStateOf("")

    fun addNewCenter() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewCenterUseCase(
                LabImagingCenter(
                    name = nameState.value,
                    type = typeState.value,
                    phone = phoneState.value,
                    address = addressState.value,
                ),
            ).collect { result ->
                _actionResFlow.emit(result)
            }
        }
    }

    fun updateCenter() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            selectedCenterState.value?.let {
                updateCenterUseCase(it).collect { result ->
                    _actionResFlow.emit(result)
                }
            } ?: run {
                _actionResFlow.emit(Resource.Error(Error("No center selected!")))
            }
        }
    }

    fun listCenters() {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase().collect { result ->
                _centersResFlow.value = result
            }
        }
    }

    fun addNewCenterStaff() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewCenterStaffUseCase(
                CenterStaff(
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    centerId = centerIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    profilePicture = profilePictureState.value,
                ),
            ).collect { result ->
                _actionResFlow.emit(result)
            }
        }
    }

    fun updateCenterStaff() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            selectedCenterStaffState.value?.let {
                updateCenterStaffUseCase(it).collect { result ->
                    _actionResFlow.emit(result)
                }
            } ?: run {
                _actionResFlow.emit(Resource.Error(Error("No clinic staff selected!")))
            }
        }
    }

    fun listStaff() {
        viewModelScope.launch(dispatcher) {
            if (_centerStaffsResFlow.value !is Resource.Unspecified) {
                _centerStaffsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCenterStaffUseCase(selectedCenterIdState.longValue).collect { result ->
                _centerStaffsResFlow.value = result
            }
        }
    }
}
