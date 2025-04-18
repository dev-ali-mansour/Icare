package eg.edu.cu.csds.icare.admin.screen.center.staff

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.AddNewCenterStaff
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.ListCenterStaff
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.UpdateCenterStaff
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CenterStaffViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewCenterStaffUseCase: AddNewCenterStaff,
    private val updateCenterStaffUseCase: UpdateCenterStaff,
    private val listCenterStaffUseCase: ListCenterStaff,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: StateFlow<Resource<Nothing?>> = _actionResFlow
    private val _centerStaffResFlow =
        MutableStateFlow<Resource<List<CenterStaff>>>(Resource.Unspecified())
    val centerStaffResFlow: StateFlow<Resource<List<CenterStaff>>> = _centerStaffResFlow

    var selectedCenterIdState = mutableLongStateOf(0)

    var idState = mutableLongStateOf(0)

    var firstNameState = mutableStateOf("")

    var lastNameState = mutableStateOf("")

    var centerIdState = mutableLongStateOf(0)

    var emailState = mutableStateOf("")

    var phoneState = mutableStateOf("")

    var profilePictureState = mutableStateOf("")

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
                _actionResFlow.value = result
            }
        }
    }

    fun updateCenterStaff() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }

            updateCenterStaffUseCase(
                CenterStaff(
                    id = idState.longValue,
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    centerId = centerIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    profilePicture = profilePictureState.value,
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }
    }

    fun listCenterStaff() {
        viewModelScope.launch(dispatcher) {
            if (_centerStaffResFlow.value !is Resource.Unspecified) {
                _centerStaffResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCenterStaffUseCase(selectedCenterIdState.longValue).collect { result ->
                _centerStaffResFlow.value = result
            }
        }
    }
}
