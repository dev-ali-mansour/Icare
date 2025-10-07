package eg.edu.cu.csds.icare.feature.auth.screen.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.feature.auth.R
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMailUseCase
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
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
class PasswordRecoveryViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val sendRecoveryMailUseCase: SendRecoveryMailUseCase,
) : ViewModel() {
    private var sendRecoveryMailJob: Job? = null
    private val _uiState = MutableStateFlow(PasswordRecoveryState())
    val uiState =
        _uiState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _uiState.value,
        )
    val effect = _uiState.map { it.effect }

    fun processEvent(event: PasswordRecoveryEvent) {
        when (event) {
            is PasswordRecoveryEvent.UpdateEmail -> {
                _uiState.update { it.copy(email = event.email) }
            }

            is PasswordRecoveryEvent.SubmitRecovery ->
                viewModelScope.launch {
                    when {
                        !_uiState.value.email.isValidEmail ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PasswordRecoveryEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_error_invalid_email,
                                                ),
                                        ),
                                )
                            }

                        else -> {
                            sendRecoveryMailJob?.cancel()
                            sendRecoveryMailJob = launchSendRecoveryMail()
                        }
                    }
                }

            PasswordRecoveryEvent.NavigateToSignInScreen -> Unit

            PasswordRecoveryEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchSendRecoveryMail() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            sendRecoveryMailUseCase(email = _uiState.value.email)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PasswordRecoveryEffect.ShowInfo(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_auth_recovery_email_sent,
                                                ),
                                        ),
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = PasswordRecoveryEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
