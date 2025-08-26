package eg.edu.cu.csds.icare.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
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
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class SplashViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val readOnBoardingUseCase: ReadOnBoardingUseCase,
    private val listClinicsUseCase: ListClinicsUseCase,
    private val listDoctorsUseCase: ListDoctorsUseCase,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
    private val listCentersUseCase: ListCentersUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SplashState())
    val state =
        _state
            .onStart {
                observeOnBoardingStatus()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )

    private val _singleEvent = Channel<SplashSingleEvent>(Channel.BUFFERED)
    val singleEvent = _singleEvent.receiveAsFlow()

    @Suppress("ktlint:standard:max-line-length")
    private fun observeOnBoardingStatus() =
        viewModelScope.launch(dispatcher) {
            readOnBoardingUseCase()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .onEach { result ->
                    result
                        .onSuccess { isOnBoardingCompleted ->
                            _state.update { it.copy(isOnBoardingCompleted = isOnBoardingCompleted) }
                            if (isOnBoardingCompleted) {
                                Timber
                                    .tag("Splash-Debug")
                                    .d("On-boarding completed, fetching initial data...")
                                listClinicsUseCase(forceUpdate = true)
                                    .onEach { clinicsResult ->
                                        clinicsResult
                                            .onSuccess {
                                                listDoctorsUseCase(
                                                    forceUpdate = true,
                                                ).onEach { doctorsResult ->
                                                    doctorsResult
                                                        .onSuccess {
                                                            listPharmaciesUseCase(
                                                                forceUpdate = true,
                                                            ).onEach { pharmaciesResult ->
                                                                pharmaciesResult
                                                                    .onSuccess {
                                                                        listCentersUseCase(
                                                                            forceUpdate = true,
                                                                        ).onEach { centersResult ->
                                                                            centersResult
                                                                                .onSuccess {
                                                                                    Timber
                                                                                        .tag("Splash-Debug")
                                                                                        .d(
                                                                                            "Clinics loaded successfully",
                                                                                        )
                                                                                    _state.update {
                                                                                        it.copy(
                                                                                            isLoading = false,
                                                                                        )
                                                                                    }
                                                                                    Timber
                                                                                        .tag(
                                                                                            "Splash-Debug",
                                                                                        ).d(
                                                                                            "Navigating to Home",
                                                                                        )
                                                                                    _singleEvent.send(
                                                                                        SplashSingleEvent.NavigateToHome,
                                                                                    )
                                                                                }.onError { error ->
                                                                                    when (error) {
                                                                                        DataError.Remote.USER_NOT_AUTHORIZED -> {
                                                                                            _singleEvent.send(
                                                                                                SplashSingleEvent.NavigateToSignIn,
                                                                                            )
                                                                                        }

                                                                                        else -> {
                                                                                            _singleEvent.send(
                                                                                                SplashSingleEvent
                                                                                                    .ShowError(
                                                                                                        error
                                                                                                            .toUiText(),
                                                                                                    ),
                                                                                            )
                                                                                        }
                                                                                    }
                                                                                }
                                                                        }.launchIn(viewModelScope)
                                                                    }.onError { error ->
                                                                        when (error) {
                                                                            DataError.Remote.USER_NOT_AUTHORIZED,
                                                                            -> {
                                                                                _singleEvent.send(
                                                                                    SplashSingleEvent.NavigateToSignIn,
                                                                                )
                                                                            }

                                                                            else -> {
                                                                                _singleEvent.send(
                                                                                    SplashSingleEvent
                                                                                        .ShowError(
                                                                                            error.toUiText(),
                                                                                        ),
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                            }.launchIn(viewModelScope)
                                                        }.onError { error ->
                                                            when (error) {
                                                                DataError.Remote.USER_NOT_AUTHORIZED -> {
                                                                    _singleEvent.send(
                                                                        SplashSingleEvent.NavigateToSignIn,
                                                                    )
                                                                }

                                                                else -> {
                                                                    _singleEvent.send(
                                                                        SplashSingleEvent.ShowError(
                                                                            error.toUiText(),
                                                                        ),
                                                                    )
                                                                }
                                                            }
                                                        }
                                                }.launchIn(viewModelScope)
                                            }.onError { error ->
                                                Timber.tag("Splash-Debug").e("error: $error")
                                                _state.update { it.copy(isLoading = false) }
                                                when (error) {
                                                    DataError.Remote.USER_NOT_AUTHORIZED -> {
                                                        _singleEvent.send(
                                                            SplashSingleEvent.NavigateToSignIn,
                                                        )
                                                    }

                                                    else -> {
                                                        _singleEvent.send(
                                                            SplashSingleEvent.ShowError(error.toUiText()),
                                                        )
                                                    }
                                                }
                                            }
                                    }.launchIn(viewModelScope)
                            } else {
                                Timber.tag("Splash-Debug").d("On-boarding not completed")
                                _state.update { it.copy(isLoading = false) }
                                _singleEvent.send(SplashSingleEvent.NavigateToOnBoarding)
                            }
                        }.onError { error ->
                            Timber.tag("Splash-Debug").e("error: $error")
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.send(SplashSingleEvent.ShowError(error.toUiText()))
                        }
                }.launchIn(viewModelScope)
        }
}
