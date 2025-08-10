package eg.edu.cu.csds.icare.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.clinic.ListClinicsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.onboarding.ReadOnBoardingUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
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
    private val _singleEvent =
        MutableSharedFlow<SplashSingleEvent>(replay = 1)
    val singleEvent = _singleEvent.asSharedFlow()

    private fun observeOnBoardingStatus() =
        viewModelScope.launch(dispatcher) {
            readOnBoardingUseCase()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .onEach { result ->
                    result
                        .onSuccess { isOnBoardingCompleted ->
                            _state.update { it.copy(isOnBoardingCompleted = isOnBoardingCompleted) }

                            if (!isOnBoardingCompleted) {
                                _singleEvent.emit(SplashSingleEvent.NavigateToOnBoarding)
                                _state.update { it.copy(isLoading = false) } // Stop loading
                                return@onSuccess // Important to stop further processing
                            }

                            combine(
                                listClinicsUseCase(forceUpdate = true),
                                listDoctorsUseCase(forceUpdate = true),
                                listPharmaciesUseCase(forceUpdate = true),
                                listCentersUseCase(forceUpdate = true),
                            ) { clinicsResult, doctorsResult, pharmaciesResult, centersResult ->
                                val allResults =
                                    listOf(
                                        clinicsResult,
                                        doctorsResult,
                                        pharmaciesResult,
                                        centersResult,
                                    )

                                val userNotAuthorized =
                                    allResults.any { res ->
                                        res is Result.Error &&
                                            res.error == DataError.Remote.USER_NOT_AUTHORIZED
                                    }

                                if (userNotAuthorized) {
                                    _singleEvent.emit(SplashSingleEvent.NavigateToSignIn)
                                    return@combine false
                                }

                                val allSuccessful =
                                    clinicsResult is Result.Success &&
                                        doctorsResult is Result.Success &&
                                        pharmaciesResult is Result.Success &&
                                        centersResult is Result.Success

                                if (allSuccessful) {
                                    _singleEvent.emit(SplashSingleEvent.NavigateToHome)
                                } else {
                                    val errorMessages = mutableListOf<String>()
                                    allResults.forEach { res ->
                                        if (res is Result.Error) {
                                            errorMessages.add(res.error.toUiText().toString())
                                        }
                                    }
                                    if (errorMessages.isNotEmpty()) {
                                        _singleEvent.emit(
                                            SplashSingleEvent.ShowError(
                                                UiText.DynamicString(
                                                    errorMessages.joinToString(", "),
                                                ),
                                            ),
                                        )
                                    }
                                }
                                allSuccessful
                            }.onCompletion { cause ->
                                _state.update { it.copy(isLoading = false) }
                                if (cause != null) {
                                    _singleEvent.emit(
                                        SplashSingleEvent.ShowError(
                                            UiText.DynamicString(
                                                "An unexpected error occurred: ${cause.message}",
                                            ),
                                        ),
                                    )
                                }
                            }.launchIn(viewModelScope)
                        }.onError { error ->
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(SplashSingleEvent.ShowError(error.toUiText()))
                        }
                }.launchIn(viewModelScope)
        }
}
