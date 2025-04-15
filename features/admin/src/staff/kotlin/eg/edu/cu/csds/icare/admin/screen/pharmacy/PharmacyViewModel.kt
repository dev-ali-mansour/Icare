package eg.edu.cu.csds.icare.admin.screen.pharmacy

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.AddNewPharmacy
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmacies
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.UpdatePharmacy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PharmacyViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val addNewPharmacyUseCase: AddNewPharmacy,
    private val updatePharmacyUseCase: UpdatePharmacy,
    private val listPharmaciesUseCase: ListPharmacies,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: StateFlow<Resource<Nothing?>> = _actionResFlow
    private val _pharmaciesResFlow =
        MutableStateFlow<Resource<List<Pharmacy>>>(Resource.Unspecified())
    val pharmaciesResFlow: StateFlow<Resource<List<Pharmacy>>> = _pharmaciesResFlow

    var idState = mutableLongStateOf(0)
        private set
    var nameState = mutableStateOf("")
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

    fun addNewPharmacy() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewPharmacyUseCase(
                Pharmacy(
                    name = nameState.value,
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

        fun updatePharmacy() {
            viewModelScope.launch(dispatcher) {
                if (_actionResFlow.value !is Resource.Unspecified) {
                    _actionResFlow.value = Resource.Unspecified()
                    delay(timeMillis = 100)
                }
                updatePharmacyUseCase(
                    Pharmacy(
                        id = idState.longValue,
                        name = nameState.value,
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

    fun listPharmacies() {
        viewModelScope.launch(dispatcher) {
            if (_pharmaciesResFlow.value !is Resource.Unspecified) {
                _pharmaciesResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listPharmaciesUseCase().collect { result ->
                _pharmaciesResFlow.value = result
            }
        }
    }
}
