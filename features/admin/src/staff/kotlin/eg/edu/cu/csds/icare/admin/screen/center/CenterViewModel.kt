package eg.edu.cu.csds.icare.admin.screen.center

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.center.AddNewCenter
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCenters
import eg.edu.cu.csds.icare.core.domain.usecase.center.UpdateCenter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CenterViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewCenterUseCase: AddNewCenter,
    private val updateCenterUseCase: UpdateCenter,
    private val listCentersUseCase: ListCenters,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: StateFlow<Resource<Nothing?>> = _actionResFlow
    private val _centersResFlow =
        MutableStateFlow<Resource<List<LabImagingCenter>>>(Resource.Unspecified())
    val centersResFlow: StateFlow<Resource<List<LabImagingCenter>>> = _centersResFlow

    var idState = mutableLongStateOf(0)
        private set
    var nameState = mutableStateOf("")
        private set
    var typeState = mutableStateOf(0.toShort())
        private set
    var phoneState = mutableStateOf("")
        private set
    var addressState = mutableStateOf("")
        private set
    var longitudeState = mutableDoubleStateOf(0.0)
        private set
    var latitudeState = mutableDoubleStateOf(0.0)
        private set
    var contractStatusIdState = mutableStateOf(0.toShort())
        private set

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
                    longitude = longitudeState.doubleValue,
                    latitude = latitudeState.doubleValue,
                    contractStatusId = contractStatusIdState.value,
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }

        fun updateCenter() {
            viewModelScope.launch(dispatcher) {
                if (_actionResFlow.value !is Resource.Unspecified) {
                    _actionResFlow.value = Resource.Unspecified()
                    delay(timeMillis = 100)
                }
                updateCenterUseCase(
                    LabImagingCenter(
                        id = idState.longValue,
                        name = nameState.value,
                        type = typeState.value,
                        phone = phoneState.value,
                        address = addressState.value,
                        longitude = longitudeState.doubleValue,
                        latitude = latitudeState.doubleValue,
                        contractStatusId = contractStatusIdState.value,
                    ),
                ).collect { result ->
                    _actionResFlow.value = result
                }
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
}
