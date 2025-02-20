package eg.edu.cu.csds.icare.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.SaveOnBoarding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val saveOnBoarding: SaveOnBoarding,
) : ViewModel() {
    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveOnBoarding(completed = completed)
        }
    }
}
