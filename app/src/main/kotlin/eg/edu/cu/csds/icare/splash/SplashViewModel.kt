package eg.edu.cu.csds.icare.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val readOnBoardingUseCase: ReadOnBoardingUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val listClinicsUseCase: ListClinicsUseCase,
    private val listDoctorsUseCase: ListDoctorsUseCase,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
    private val listCentersUseCase: ListCentersUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashState())
    val uiState =
        _uiState
            .onStart {
                observeOnBoardingStatus()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )

    val effect = _uiState.map { it.effect }

    fun processEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
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
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        effect = SplashEffect.NavigateToRoute(Route.OnBoarding),
                                    )
                                }
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = SplashEffect.ShowError(error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun getCurrentUser() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase(forceUpdate = true).collect { result ->
                result
                    .onSuccess {
                        fetchClinics()
                    }.onError { error ->
                        _uiState.update { it.copy(isLoading = false) }
                        when (error) {
                            DataError.Remote.USER_NOT_AUTHORIZED ->
                                _uiState.update {
                                    it.copy(
                                        effect = SplashEffect.NavigateToRoute(Route.SignIn),
                                    )
                                }

                            else ->
                                _uiState.update {
                                    it.copy(
                                        effect =
                                            SplashEffect.ShowError(error.toUiText()),
                                    )
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
                            DataError.Remote.USER_NOT_AUTHORIZED ->
                                _uiState.update {
                                    it.copy(
                                        effect =
                                            SplashEffect.NavigateToRoute(Route.SignIn),
                                    )
                                }

                            else ->
                                _uiState.update {
                                    it.copy(
                                        effect =
                                            SplashEffect.ShowError(error.toUiText()),
                                    )
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
                        DataError.Remote.USER_NOT_AUTHORIZED ->
                            _uiState.update { it.copy(effect = SplashEffect.NavigateToRoute(Route.SignIn)) }

                        else ->
                            _uiState.update {
                                it.copy(
                                    effect =
                                        SplashEffect.ShowError(error.toUiText()),
                                )
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
                        DataError.Remote.USER_NOT_AUTHORIZED ->
                            _uiState.update { it.copy(effect = SplashEffect.NavigateToRoute(Route.SignIn)) }

                        else ->
                            _uiState.update {
                                it.copy(
                                    effect =
                                        SplashEffect.ShowError(error.toUiText()),
                                )
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
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            effect = SplashEffect.NavigateToRoute(Route.Home),
                        )
                    }
                }.onError { error ->
                    when (error) {
                        DataError.Remote.USER_NOT_AUTHORIZED ->
                            _uiState.update { it.copy(effect = SplashEffect.NavigateToRoute(Route.SignIn)) }

                        else ->
                            _uiState.update {
                                it.copy(
                                    effect =
                                        SplashEffect.ShowError(error.toUiText()),
                                )
                            }
                    }
                }
        }.launchIn(viewModelScope)
    }
}
