package eg.edu.cu.csds.icare.onboarding.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.SaveOnBoarding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OnBoardingViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val saveOnBoarding: SaveOnBoarding,
) : ViewModel() {
    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch(dispatcher) {
            saveOnBoarding(completed = completed)
        }
    }
}
