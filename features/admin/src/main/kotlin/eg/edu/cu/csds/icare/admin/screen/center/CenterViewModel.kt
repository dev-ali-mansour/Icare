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
import eg.edu.cu.csds.icare.core.ui.common.CenterTypeItem
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
    private val _centersResFlow =
        MutableStateFlow<Resource<List<LabImagingCenter>>>(Resource.Unspecified())
    val centersResFlow: StateFlow<Resource<List<LabImagingCenter>>> = _centersResFlow
    private val _centerStaffsResFlow =
        MutableStateFlow<Resource<List<CenterStaff>>>(Resource.Unspecified())
    val centerStaffsResFlow: StateFlow<Resource<List<CenterStaff>>> = _centerStaffsResFlow
    var selectedCenterState: MutableState<LabImagingCenter?> = mutableStateOf(null)
    var selectedCenterStaffState: MutableState<CenterStaff?> = mutableStateOf(null)

    var showSuccessDialog = mutableStateOf(false)
    var isRefreshing = mutableStateOf(false)
    var selectedCenterIdState = mutableLongStateOf(0)
    var searchQueryState = mutableStateOf("")
    var nameState = mutableStateOf("")
    var typeState = mutableStateOf(0.toShort())
    var phoneState = mutableStateOf("")
    var addressState = mutableStateOf("")
    var firstNameState = mutableStateOf("")
    var lastNameState = mutableStateOf("")
    var centerIdState = mutableLongStateOf(0)
    var emailState = mutableStateOf("")
    var typesExpandedState = mutableStateOf(false)
    var centersExpandedState = mutableStateOf(false)

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
                if (result is Resource.Success) resetStates()
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
                    if (result is Resource.Success) resetStates()
                    _actionResFlow.emit(result)
                }
            } ?: run {
                _actionResFlow.emit(Resource.Error(Error("No center selected!")))
            }
        }
    }

    fun listCenters(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase(forceRefresh).collect { result ->
                _centersResFlow.value = result
            }
        }
    }

    fun listLabCenters(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase(forceUpdate = forceRefresh).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { centers ->
                            val filtered =
                                centers.filter { center ->
                                    center.type == CenterTypeItem.LabCenter.code
                                }
                            _centersResFlow.value = Resource.Success(filtered)
                        }
                    }

                    else -> _centersResFlow.value = result
                }
            }
        }
    }

    fun searchLabCenters() {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { centers ->
                            val filtered =
                                centers.filter { center ->
                                    center.type == CenterTypeItem.LabCenter.code &&
                                        (
                                            searchQueryState.value.isEmpty() ||
                                                center.name.contains(
                                                    searchQueryState.value,
                                                    ignoreCase = true,
                                                ) ||
                                                center.address.contains(
                                                    searchQueryState.value,
                                                    ignoreCase = true,
                                                )
                                        )
                                }
                            _centersResFlow.value = Resource.Success(filtered)
                        }
                    }

                    else -> _centersResFlow.value = result
                }
            }
        }
    }

    fun listImagingCenters(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase(forceUpdate = forceRefresh).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { centers ->
                            val filtered =
                                centers.filter { center ->
                                    center.type == CenterTypeItem.ImagingCenter.code
                                }
                            _centersResFlow.value = Resource.Success(filtered)
                        }
                    }

                    else -> _centersResFlow.value = result
                }
            }
        }
    }

    fun searchImagingCenters() {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { centers ->
                            val filtered =
                                centers.filter { center ->
                                    center.type == CenterTypeItem.ImagingCenter.code &&
                                        (
                                            searchQueryState.value.isEmpty() ||
                                                center.name.contains(
                                                    searchQueryState.value,
                                                    ignoreCase = true,
                                                ) ||
                                                center.address.contains(
                                                    searchQueryState.value,
                                                    ignoreCase = true,
                                                )
                                        )
                                }
                            _centersResFlow.value = Resource.Success(filtered)
                        }
                    }

                    else -> _centersResFlow.value = result
                }
            }
        }
    }

    fun addNewStaff() {
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
                ),
            ).collect { result ->
                if (result is Resource.Success) resetStates()
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
            selectedCenterStaffState.value?.let {
                updateCenterStaffUseCase(it).collect { result ->
                    if (result is Resource.Success) resetStates()
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
            listCenterStaffUseCase().collect { result ->
                _centerStaffsResFlow.value = result
            }
        }
    }

    private fun resetStates() {
        selectedCenterStaffState.value = null
        selectedCenterState.value = null
        selectedCenterState.value = null
        nameState.value = ""
        typeState.value = 0.toShort()
        phoneState.value = ""
        addressState.value = ""
    }
}
