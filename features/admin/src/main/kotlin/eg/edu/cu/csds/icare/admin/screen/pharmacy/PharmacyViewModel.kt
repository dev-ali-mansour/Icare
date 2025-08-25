package eg.edu.cu.csds.icare.admin.screen.pharmacy

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PharmacyViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
) : ViewModel() {
    private val _pharmaciesResFlow =
        MutableStateFlow<Resource<List<Pharmacy>>>(Resource.Unspecified())
    val pharmaciesResFlow: StateFlow<Resource<List<Pharmacy>>> = _pharmaciesResFlow
    var isRefreshing = mutableStateOf(false)
    var searchQueryState = mutableStateOf("")

    fun listPharmacies(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            searchQueryState.value = ""
            if (_pharmaciesResFlow.value !is Resource.Unspecified) {
                _pharmaciesResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listPharmaciesUseCase(forceRefresh).collect { result ->
                result
                    .onSuccess { pharmacies ->
                        _pharmaciesResFlow.value = Resource.Success(pharmacies)
                    }.onError {
                        _pharmaciesResFlow.value = Resource.Error(Exception(it.toString()))
                    }
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
                result
                    .onSuccess { pharmacies ->
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
                    }.onError {
                        _pharmaciesResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }
}
