package eg.edu.cu.csds.icare.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfo
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoarding
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
            getUserInfo(forceUpdate = false).distinctUntilChanged().collect {
                _currentUserFlow.value = it
            }
            readOnBoarding().collect { res ->
                _onBoardingCompleted.value = res
                if (res is Resource.Success && res.data == true) {
                    viewModelScope.launch(dispatcher) {
                        runCatching {
                            // Todo replace this delay with actual call of fetching use cases
                            _resultFlow.value = Resource.Loading()
                            delay(timeMillis = 1000)
                            _resultFlow.value = Resource.Success(null)
                            /*fetchClinics().distinctUntilChanged().collect {
                                _resultFlow.value = it
                            }*/
                        }.onFailure { _resultFlow.value = Resource.Error(it) }
                    }
                }
            }
        }
    }
}
