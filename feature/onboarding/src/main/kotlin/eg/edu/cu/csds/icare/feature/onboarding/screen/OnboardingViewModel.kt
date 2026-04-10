package eg.edu.cu.csds.icare.feature.onboarding.screen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.FinishOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.onError
import eg.edu.cu.csds.icare.core.domain.util.onSuccess
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@Stable
@KoinViewModel
class OnboardingViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val readOnBoardingUseCase: ReadOnBoardingUseCase,
    private val finishOnBoardingUseCase: FinishOnBoardingUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val listClinicsUseCase: ListClinicsUseCase,
    private val listDoctorsUseCase: ListDoctorsUseCase,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
    private val listCentersUseCase: ListCentersUseCase,
) : ViewModel() {
    private var finishOnBoardingJob: Job? = null
    private val _uiState = MutableStateFlow(OnBoardingState())
    val uiState =
        _uiState
            .onStart {
                observeOnBoardingStatus()
            }.stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted
                        .WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    private val _effect = Channel<OnBoardingEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: OnBoardingIntent) {
        when (intent) {
            is OnBoardingIntent.FinishOnBoarding -> {
                finishOnBoardingJob?.cancel()
                finishOnBoardingJob = launchFinishingOnBoarding()
            }
        }
    }

    private fun observeOnBoardingStatus() =
        viewModelScope.launch(dispatcher) {
            readOnBoardingUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .onEach { result ->
                    result
                        .onSuccess { isOnBoardingCompleted ->
                            _uiState.update { it.copy(isOnBoardingCompleted = isOnBoardingCompleted) }
                            if (isOnBoardingCompleted) {
                                getCurrentUser()
                            } else {
                                _uiState.update { it.copy(isLoading = false) }
                                _effect.send(OnBoardingEffect.NavigateToRoute(Route.OnBoarding))
                            }
                        }.onError { error ->
                            _uiState.update { it.copy(isLoading = false) }
                            _effect.send(OnBoardingEffect.ShowError(error.toUiText()))
                        }
                }.launchIn(viewModelScope)
        }

    private fun getCurrentUser() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase(forceUpdate = false).collect { result ->
                result
                    .onSuccess {
                        fetchClinics()
                    }.onError { error ->
                        _uiState.update { it.copy(isLoading = false) }
                        when (error) {
                            DataError.Remote.USER_NOT_AUTHORIZED -> {
                                _effect.send(OnBoardingEffect.NavigateToRoute(Route.SignIn))
                            }

                            else -> {
                                _effect.send(OnBoardingEffect.ShowError(error.toUiText()))
                            }
                        }
                    }
            }
        }

    private fun fetchClinics() {
        listClinicsUseCase(forceUpdate = true)
            .onEach { clinicsResult ->
                clinicsResult
                    .onSuccess {
                        fetchDoctors()
                    }.onError { error ->
                        _uiState.update { it.copy(isLoading = false) }
                        when (error) {
                            DataError.Remote.USER_NOT_AUTHORIZED -> {
                                _effect.send(OnBoardingEffect.NavigateToRoute(Route.SignIn))
                            }

                            else -> {
                                _uiState.update { it.copy(isLoading = false) }
                                _effect.send(OnBoardingEffect.NavigateToRoute(Route.Home))
                            }
                        }
                    }
            }.launchIn(viewModelScope)
    }

    private fun fetchDoctors() {
        listDoctorsUseCase(
            forceUpdate = true,
        ).onEach { doctorsResult ->
            doctorsResult
                .onSuccess {
                    fetchPharmacies()
                }.onError { error ->
                    when (error) {
                        DataError.Remote.USER_NOT_AUTHORIZED -> {
                            _effect.send(
                                OnBoardingEffect.NavigateToRoute(Route.SignIn),
                            )
                        }

                        else -> {
                            _uiState.update { it.copy(isLoading = false) }
                            _effect.send(OnBoardingEffect.NavigateToRoute(Route.Home))
                        }
                    }
                }
        }.launchIn(viewModelScope)
    }

    private fun fetchPharmacies() {
        listPharmaciesUseCase(
            forceUpdate = true,
        ).onEach { pharmaciesResult ->
            pharmaciesResult
                .onSuccess {
                    fetchCenters()
                }.onError { error ->
                    when (error) {
                        DataError.Remote.USER_NOT_AUTHORIZED -> {
                            _effect.send(
                                OnBoardingEffect.NavigateToRoute(Route.SignIn),
                            )
                        }

                        else -> {
                            _uiState.update { it.copy(isLoading = false) }
                            _effect.send(OnBoardingEffect.NavigateToRoute(Route.Home))
                        }
                    }
                }
        }.launchIn(viewModelScope)
    }

    private fun fetchCenters() {
        listCentersUseCase(
            forceUpdate = true,
        ).onEach { centersResult ->
            centersResult
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.send(OnBoardingEffect.NavigateToRoute(Route.Home))
                }.onError { error ->
                    when (error) {
                        DataError.Remote.USER_NOT_AUTHORIZED -> {
                            _effect.send(OnBoardingEffect.NavigateToRoute(Route.SignIn))
                        }

                        else -> {
                            _uiState.update { it.copy(isLoading = false) }
                            _effect.send(OnBoardingEffect.NavigateToRoute(Route.Home))
                        }
                    }
                }
        }.launchIn(viewModelScope)
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
                            _uiState.update { it.copy(isLoading = false) }
                            _effect.send(OnBoardingEffect.ShowError(error.toUiText()))
                        }
                }.launchIn(viewModelScope)
        }
}
