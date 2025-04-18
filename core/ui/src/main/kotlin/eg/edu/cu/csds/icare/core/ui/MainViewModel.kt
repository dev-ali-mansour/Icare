package eg.edu.cu.csds.icare.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfo
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCenters
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinics
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoarding
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmacies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val readOnBoarding: ReadOnBoarding,
    private val getUserInfo: GetUserInfo,
    private val listClinics: ListClinics,
    private val listPharmacies: ListPharmacies,
    private val listCenters: ListCenters,
) : ViewModel() {
    private val _onBoardingCompleted: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Unspecified())
    val onBoardingCompleted: StateFlow<Resource<Boolean>> = _onBoardingCompleted
    private val _currentUserFlow: MutableStateFlow<Resource<User>> =
        MutableStateFlow(Resource.Unspecified())
    val currentUserFlow: StateFlow<Resource<User>> = _currentUserFlow
    private val _resultFlow = MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val resultFlow: StateFlow<Resource<Nothing?>> = _resultFlow

    init {
        viewModelScope.launch(dispatcher) {
            runCatching {
                getUserInfo(forceUpdate = false).distinctUntilChanged().collect {
                    _currentUserFlow.value = it
                }
                readOnBoarding().collect { res ->
                    _onBoardingCompleted.value = res

                    if (res is Resource.Success) {

                        if (_resultFlow.value !is Resource.Unspecified) {
                            _resultFlow.value = Resource.Unspecified()
                            delay(timeMillis = 100)
                        }
                        listClinics(forceUpdate = true).collect {
                            when (it) {
                                is Resource.Unspecified<*> ->
                                    _resultFlow.value = Resource.Unspecified()

                                is Resource.Loading<*> ->
                                    _resultFlow.value = Resource.Loading()

                                is Resource.Success ->
                                    listPharmacies(forceUpdate = true).collect {
                                        when (it) {
                                            is Resource.Unspecified<*> ->
                                                _resultFlow.value = Resource.Unspecified()

                                            is Resource.Loading<*> ->
                                                _resultFlow.value = Resource.Loading()

                                            is Resource.Success ->
                                                listCenters(forceUpdate = true).collect {
                                                    when (it) {
                                                        is Resource.Unspecified<*> ->
                                                            _resultFlow.value =
                                                                Resource.Unspecified()

                                                        is Resource.Loading<*> ->
                                                            _resultFlow.value = Resource.Loading()

                                                        is Resource.Success ->
                                                            _resultFlow.value =
                                                                Resource.Success(null)

                                                        is Resource.Error<*> ->
                                                            _resultFlow.value =
                                                                Resource.Error(it.error)
                                                    }
                                                }

                                            is Resource.Error<*> ->
                                                _resultFlow.value = Resource.Error(it.error)
                                        }
                                    }

                                is Resource.Error<*> ->
                                    _resultFlow.value = Resource.Error(it.error)
                            }
                        }
                    }
                }
            }.onFailure { _resultFlow.value = Resource.Error(it) }
        }
    }
}
