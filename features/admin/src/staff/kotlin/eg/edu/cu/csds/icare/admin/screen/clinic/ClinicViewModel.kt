package eg.edu.cu.csds.icare.admin.screen.clinic

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.AddNewClinic
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinics
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.UpdateClinic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ClinicViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewClinicUseCase: AddNewClinic,
    private val updateClinicUseCase: UpdateClinic,
    private val listClinicsUseCase: ListClinics,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: StateFlow<Resource<Nothing?>> = _actionResFlow
    private val _clinicsResFlow =
        MutableStateFlow<Resource<List<Clinic>>>(Resource.Unspecified())
    val clinicsResFlow: StateFlow<Resource<List<Clinic>>> = _clinicsResFlow

    var idState = mutableLongStateOf(0)
        private set
    var nameState = mutableStateOf("")
        private set
    var typeState = mutableStateOf("")
        private set
    var phoneState = mutableStateOf("")
        private set
    var addressState = mutableStateOf("")
        private set
    var longitudeState = mutableDoubleStateOf(0.0)
        private set
    var latitudeState = mutableDoubleStateOf(0.0)
        private set
    var isOpenState = mutableStateOf(false)
        private set

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
}
