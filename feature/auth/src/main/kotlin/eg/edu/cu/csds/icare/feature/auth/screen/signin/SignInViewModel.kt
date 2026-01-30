package eg.edu.cu.csds.icare.feature.auth.screen.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithGoogleUseCase
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.R.string
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.feature.auth.R
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

    fun handleIntent(intent: SignInIntent) {
        when (intent) {
            is SignInIntent.UpdateGoogleSignInToken -> {
                _uiState.update { it.copy(googleSignInToken = intent.token) }
            }

            is SignInIntent.UpdateEmail -> {
                _uiState.update { it.copy(email = intent.email) }
            }

            is SignInIntent.UpdatePassword -> {
                _uiState.update { it.copy(password = intent.password) }
            }

            is SignInIntent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            is SignInIntent.SignInWithGoogle -> {
                signInJob?.cancel()
                signInJob = launchGoogleSignIn()
            }

            is SignInIntent.SubmitSignIn -> {
                viewModelScope.launch {
                    when {
                        !_uiState.value.email.isValidEmail -> {
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
                        }

                        _uiState.value.password.isBlank() -> {
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
                        }

                        else -> {
                            signInJob?.cancel()
                            signInJob = launchSignIn()
                        }
                    }
                }
            }

            is SignInIntent.NavigateToPasswordRecoveryScreen -> {}

            is SignInIntent.NavigateToSignUpScreen -> {}

            is SignInIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
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
                                    effect = SignInEffect.SignInSuccess,
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
                                        effect = SignInEffect.SignInSuccess,
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
