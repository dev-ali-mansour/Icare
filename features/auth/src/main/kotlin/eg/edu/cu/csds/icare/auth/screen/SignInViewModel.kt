package eg.edu.cu.csds.icare.auth.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber
import eg.edu.cu.csds.icare.core.ui.R as CoreR

@KoinViewModel
class SignInViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {
    private var loginJob: Job? = null

    private val _state = MutableStateFlow(SignInUIState())
    val state =
        _state
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                _state.value,
            )

    fun onAction(action: SignInAction) {
        when (action) {
            is SignInAction.UpdateGoogleSignInIntent -> {
                _state.update {
                    it.copy(
                        googleSignInIntent = action.intent,
                    )
                }
            }

            is SignInAction.UpdateEmail -> {
                _state.update {
                    it.copy(
                        email = action.email,
                    )
                }
            }

            is SignInAction.UpdatePassword -> {
                _state.update {
                    it.copy(
                        password = action.password,
                    )
                }
            }

            is SignInAction.TogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible,
                    )
                }
            }

            is SignInAction.SignInWithGoogle -> {
                loginJob?.cancel()
                loginJob = onLoginWithGoogle()
            }

            is SignInAction.SubmitSignIn -> {
                Timber.e("Call SubmitSignIn")

                when {
                    !_state.value.email.isValidEmail -> {
                        _state.update {
                            it.copy(
                                errorMessage = StringResourceId(R.string.email_error),
                                isLoading = false,
                            )
                        }
                    }

                    _state.value.password.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = StringResourceId(R.string.empty_password_error),
                                isLoading = false,
                            )
                        }
                    }

                    else -> {
                        loginJob?.cancel()
                        loginJob = onLogIn()
                    }
                }
            }

            is SignInAction.NavigateToPasswordRecoveryScreen -> Unit
            is SignInAction.NavigateToSignUpScreen -> Unit
        }
    }

    private fun onLogIn() =
        viewModelScope.launch(dispatcher) {
            Timber.e("Call onLogin")
            _state.update {
                it.copy(isLoading = true)
            }
            signInUseCase(_state.value.email, _state.value.password)
                .onEach { result ->
                    result
                        .onSuccess {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    signInSuccess = true,
                                    errorMessage = null,
                                )
                            }
                        }.onError { error ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    signInSuccess = false,
                                    errorMessage = error.toUiText(),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun onLoginWithGoogle() =
        viewModelScope.launch(dispatcher) {
            _state.update {
                it.copy(isLoading = true)
            }
            runCatching {
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(_state.value.googleSignInIntent)
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { token ->
                    signInWithGoogleUseCase(token)
                        .onEach { result ->
                            result
                                .onSuccess {
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            signInSuccess = true,
                                            errorMessage = null,
                                        )
                                    }
                                }.onError { error ->
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            signInSuccess = false,
                                            errorMessage = error.toUiText(),
                                        )
                                    }
                                }
                        }.launchIn(viewModelScope)
                }
            }.onFailure {
                _state.update {
                    it.copy(
                        isLoading = false,
                        signInSuccess = false,
                        errorMessage = StringResourceId(CoreR.string.error_invalid_email),
                    )
                }
            }
        }
}
