package eg.edu.cu.csds.icare.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoarding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val readOnBoarding: ReadOnBoarding,
) : ViewModel() {
    private val _onBoardingCompleted: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(
            Resource
                .Unspecified(),
        )
    val onBoardingCompleted: StateFlow<Resource<Boolean>> = _onBoardingCompleted
    private val _resultFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val resultFlow: StateFlow<Resource<Nothing?>> = _resultFlow

    init {
        viewModelScope.launch(dispatcher) {
            readOnBoarding().collect { res ->
                _onBoardingCompleted.value = res
                if (res is Resource.Success && res.data == true) {
                    viewModelScope.launch(dispatcher) {
                        runCatching {
                            // Todo replace this delay with actual call of fetching use cases

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
