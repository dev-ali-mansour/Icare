package eg.edu.cu.csds.icare.auth.screen.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithGoogleUseCase
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
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
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@KoinViewModel
class SignInViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {
    private var signInJob: Job? = null

    private val _state = MutableStateFlow(SignInState())
    val state =
        _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _state.value,
        )
    private val _singleEvent = MutableSharedFlow<SignInSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: SignInIntent) {
        when (intent) {
            is SignInIntent.UpdateGoogleSignInToken -> {
                _state.update { it.copy(googleSignInToken = intent.token) }
            }

            is SignInIntent.UpdateEmail -> {
                _state.update { it.copy(email = intent.email) }
            }

            is SignInIntent.UpdatePassword -> {
                _state.update { it.copy(password = intent.password) }
            }

            is SignInIntent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is SignInIntent.SignInWithGoogle -> {
                signInJob?.cancel()
                signInJob = launchGoogleSignIn()
            }

            is SignInIntent.SubmitSignIn ->
                viewModelScope.launch {
                    when {
                        !_state.value.email.isValidEmail -> {
                            _singleEvent.emit(
                                SignInSingleEvent
                                    .ShowError(message = StringResourceId(R.string.error_invalid_email)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        _state.value.password.isBlank() -> {
                            _singleEvent.emit(
                                SignInSingleEvent
                                    .ShowError(message = StringResourceId(R.string.empty_password_error)),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        else -> {
                            signInJob?.cancel()
                            signInJob = launchSignIn()
                        }
                    }
                }

            is SignInIntent.NavigateToPasswordRecoveryScreen -> Unit
            is SignInIntent.NavigateToSignUpScreen -> Unit
        }
    }

    private fun launchSignIn() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            signInUseCase(_state.value.email, _state.value.password)
                .onEach { result ->
                    result
                        .onSuccess {
                            _singleEvent.emit(SignInSingleEvent.LoginSuccess)
                            _state.update { it.copy(isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(SignInSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }

    private fun launchGoogleSignIn() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            runCatching {
                signInWithGoogleUseCase(_state.value.googleSignInToken)
                    .onEach { result ->
                        result
                            .onSuccess {
                                _singleEvent.emit(SignInSingleEvent.LoginSuccess)
                                _state.update { it.copy(isLoading = false) }
                            }.onError { error ->
                                _singleEvent.emit(
                                    SignInSingleEvent
                                        .ShowError(message = error.toUiText()),
                                )
                                _state.update { it.copy(isLoading = false) }
                            }
                    }.launchIn(viewModelScope)
            }.onFailure {
                _singleEvent.emit(
                    SignInSingleEvent
                        .ShowError(message = StringResourceId(CoreR.string.error_invalid_credentials)),
                )
                _state.update { it.copy(isLoading = false) }
            }
        }
}
