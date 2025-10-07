package eg.edu.cu.csds.icare.feature.auth.screen.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.feature.auth.R
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithGoogleUseCase
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
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
class SignInViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {
    private var signInJob: Job? = null

    private val _uiState = MutableStateFlow(SignInState())
    val uiState =
        _uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _uiState.value,
        )

    val effect = _uiState.map { it.effect }

    fun processEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.UpdateGoogleSignInToken -> {
                _uiState.update { it.copy(googleSignInToken = event.token) }
            }

            is SignInEvent.UpdateEmail -> {
                _uiState.update { it.copy(email = event.email) }
            }

            is SignInEvent.UpdatePassword -> {
                _uiState.update { it.copy(password = event.password) }
            }

            is SignInEvent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is SignInEvent.SignInWithGoogle -> {
                signInJob?.cancel()
                signInJob = launchGoogleSignIn()
            }

            is SignInEvent.SubmitSignIn ->
                viewModelScope.launch {
                    when {
                        !_uiState.value.email.isValidEmail ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        SignInEffect.ShowError(
                                            message =
                                                StringResourceId(R.string.feature_auth_error_invalid_email),
                                        ),
                                )
                            }

                        _uiState.value.password.isBlank() ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        SignInEffect.ShowError(
                                            message =
                                                StringResourceId(R.string.feature_auth_empty_password_error),
                                        ),
                                )
                            }

                        else -> {
                            signInJob?.cancel()
                            signInJob = launchSignIn()
                        }
                    }
                }

            is SignInEvent.NavigateToPasswordRecoveryScreen -> Unit
            is SignInEvent.NavigateToSignUpScreen -> Unit
            is SignInEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchSignIn() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            signInUseCase(_uiState.value.email, _uiState.value.password)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = SignInEffect.LoginSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = SignInEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun launchGoogleSignIn() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                signInWithGoogleUseCase(_uiState.value.googleSignInToken)
                    .onEach { result ->
                        result
                            .onSuccess {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        effect = SignInEffect.LoginSuccess,
                                    )
                                }
                            }.onError { error ->
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        effect = SignInEffect.ShowError(message = error.toUiText()),
                                    )
                                }
                            }
                    }.launchIn(viewModelScope)
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        effect =
                            SignInEffect.ShowError(
                                message =
                                    StringResourceId(
                                        string.core_ui_error_invalid_credentials,
                                    ),
                            ),
                    )
                }
            }
        }
}
