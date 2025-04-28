package eg.edu.cu.csds.icare.admin.screen.pharmacy

import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
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
    val actionResFlow: SharedFlow<Resource<Nothing?>> =
        _actionResFlow.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 0,
        )
    private val _pharmaciesResFlow =
        MutableStateFlow<Resource<List<Pharmacy>>>(Resource.Unspecified())
    val pharmaciesResFlow: StateFlow<Resource<List<Pharmacy>>> = _pharmaciesResFlow
    private val _pharmacistsResFlow =
        MutableStateFlow<Resource<List<Pharmacist>>>(Resource.Unspecified())
    val pharmacistsResFlow: StateFlow<Resource<List<Pharmacist>>> = _pharmacistsResFlow
    var selectedPharmacyState: MutableState<Pharmacy?> = mutableStateOf(null)
    var selectedPharmacistState: MutableState<Pharmacist?> = mutableStateOf(null)

    var selectedPharmacyIdState = mutableLongStateOf(0)
    var searchQueryState = mutableStateOf("")
    var nameState = mutableStateOf("")
    var phoneState = mutableStateOf("")
    var addressState = mutableStateOf("")
    var firstNameState = mutableStateOf("")
    var lastNameState = mutableStateOf("")
    var pharmacyIdState = mutableLongStateOf(0)
    var emailState = mutableStateOf("")
    var profilePictureState = mutableStateOf("")
    var pharmaciesExpandedState = mutableStateOf(false)

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
                ),
            ).collect { result ->
                _actionResFlow.emit(result)
            }
        }
    }

    fun updatePharmacy() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            selectedPharmacyState.value?.let {
                updatePharmacyUseCase(it).collect { result ->
                    _actionResFlow.emit(result)
                }
            } ?: run {
                _actionResFlow.emit(Resource.Error(Error("No Pharmacy selected!")))
            }
        }
    }

    fun listPharmacies(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            if (_pharmaciesResFlow.value !is Resource.Unspecified) {
                _pharmaciesResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listPharmaciesUseCase(forceRefresh).collect { result ->
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
                _actionResFlow.emit(result)
            }
        }
    }

    fun updatePharmacist() {
        viewModelScope.launch(dispatcher) {
            if (_actionResFlow.value !is Resource.Unspecified) {
                _actionResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            selectedPharmacistState.value?.let {
                updatePharmacistUseCase(it).collect { result ->
                    _actionResFlow.emit(result)
                }
            } ?: run {
                _actionResFlow.emit(Resource.Error(Error("No pharmacist selected!")))
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

    fun searchPharmacies() {
        viewModelScope.launch(dispatcher) {
            if (_pharmaciesResFlow.value !is Resource.Unspecified) {
                _pharmaciesResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listPharmaciesUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { pharmacies ->
                            val filtered =
                                pharmacies.filter { pharmacy ->
                                    searchQueryState.value.isEmpty() ||
                                        pharmacy.name.contains(
                                            searchQueryState.value,
                                            ignoreCase = true,
                                        ) ||
                                        pharmacy.address.contains(
                                            searchQueryState.value,
                                            ignoreCase = true,
                                        )
                                }
                            _pharmaciesResFlow.value = Resource.Success(filtered)
                        }
                    }

                    else -> _pharmaciesResFlow.value = result
                }
            }
        }
    }
}
