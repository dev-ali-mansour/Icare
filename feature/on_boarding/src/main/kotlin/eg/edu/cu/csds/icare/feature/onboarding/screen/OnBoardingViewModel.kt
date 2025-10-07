package eg.edu.cu.csds.icare.feature.onboarding.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.FinishOnBoardingUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
    private val _uiState = MutableStateFlow(OnBoardingState())
    val uiState =
        _uiState
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted
                        .WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = _uiState.map { it.effect }

    fun processEvent(event: OnBoardingEvent) {
        when (event) {
            is OnBoardingEvent.FinishOnBoarding -> {
                finishOnBoardingJob?.cancel()
                finishOnBoardingJob = launchFinishingOnBoarding()
            }

            is OnBoardingEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchFinishingOnBoarding() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            finishOnBoardingUseCase()
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update { it.copy(isLoading = false) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = OnBoardingEffect.ShowError(error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
