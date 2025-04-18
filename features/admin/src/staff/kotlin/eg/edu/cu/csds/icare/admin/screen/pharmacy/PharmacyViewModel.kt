package eg.edu.cu.csds.icare.admin.screen.pharmacy

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.AddNewPharmacist
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.ListPharmacists
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.UpdatePharmacist
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
    private val listPharmacistsUseCase: ListPharmacists,
    private val addNewPharmacistUseCase: AddNewPharmacist,
    private val updatePharmacistUseCase: UpdatePharmacist,
) : ViewModel() {
    private val _actionResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val actionResFlow: StateFlow<Resource<Nothing?>> = _actionResFlow
    private val _pharmaciesResFlow =
        MutableStateFlow<Resource<List<Pharmacy>>>(Resource.Unspecified())
    val pharmaciesResFlow: StateFlow<Resource<List<Pharmacy>>> = _pharmaciesResFlow
    private val _pharmacistsResFlow =
        MutableStateFlow<Resource<List<Pharmacist>>>(Resource.Unspecified())
    val pharmacistsResFlow: StateFlow<Resource<List<Pharmacist>>> = _pharmacistsResFlow
    var selectedPharmacyState: MutableState<Pharmacy?> = mutableStateOf(null)
    var selectedPharmacistState: MutableState<Pharmacist?> = mutableStateOf(null)

    var selectedPharmacyIdState = mutableLongStateOf(0)
    var idState = mutableLongStateOf(0)
    var nameState = mutableStateOf("")
    var phoneState = mutableStateOf("")
    var addressState = mutableStateOf("")
    var longitudeState = mutableDoubleStateOf(0.0)
    var latitudeState = mutableDoubleStateOf(0.0)
    var contractStatusIdState = mutableStateOf(0.toShort())
    var firstNameState = mutableStateOf("")
    var lastNameState = mutableStateOf("")
    var pharmacyIdState = mutableLongStateOf(0)
    var emailState = mutableStateOf("")
    var profilePictureState = mutableStateOf("")

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

    fun addNewPharmacist() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            addNewPharmacistUseCase(
                Pharmacist(
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    pharmacyId = pharmacyIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    profilePicture = profilePictureState.value,
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }
    }

    fun updatePharmacist() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            updatePharmacistUseCase(
                Pharmacist(
                    id = idState.longValue,
                    firstName = firstNameState.value,
                    lastName = lastNameState.value,
                    pharmacyId = pharmacyIdState.longValue,
                    email = emailState.value,
                    phone = phoneState.value,
                    profilePicture = profilePictureState.value,
                ),
            ).collect { result ->
                _actionResFlow.value = result
            }
        }
    }

    fun listPharmacists() {
        viewModelScope.launch(dispatcher) {
            if (_pharmacistsResFlow.value !is Resource.Unspecified) {
                _pharmacistsResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listPharmacistsUseCase(selectedPharmacyIdState.longValue).collect { result ->
                _pharmacistsResFlow.value = result
            }
        }
    }
}
