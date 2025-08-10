package eg.edu.cu.csds.icare.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.FinishOnBoardingUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OnBoardingViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val finishOnBoardingUseCase: FinishOnBoardingUseCase,
) : ViewModel() {
    private var finishOnBoardingJob: Job? = null
    private val _state = MutableStateFlow(OnBoardingState())
    val state =
        _state
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted
                        .WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<OnBoardingSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: OnBoardingIntent) {
        when (intent) {
            is OnBoardingIntent.FinishOnBoarding -> {
                finishOnBoardingJob?.cancel()
                finishOnBoardingJob = launchFinishingOnBoarding()
            }
        }
    }

    private fun launchFinishingOnBoarding() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            finishOnBoardingUseCase()
                .onEach { result ->
                    result
                        .onSuccess {
                            _state.update { it.copy(isLoading = false) }
                        }.onError { error ->
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(OnBoardingSingleEvent.ShowError(error.toUiText()))
                        }
                }.launchIn(viewModelScope)
        }
}
