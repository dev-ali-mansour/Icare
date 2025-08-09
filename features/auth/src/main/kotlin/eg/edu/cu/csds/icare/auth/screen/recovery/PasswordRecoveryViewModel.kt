package eg.edu.cu.csds.icare.auth.screen.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.auth.R
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMailUseCase
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

@KoinViewModel
class PasswordRecoveryViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val sendRecoveryMailUseCase: SendRecoveryMailUseCase,
) : ViewModel() {
    private var sendRecoveryMailJob: Job? = null
    private val _state = MutableStateFlow(PasswordRecoveryState())
    val state =
        _state.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = _state.value,
        )
    private val _singleEvent = MutableSharedFlow<PasswordRecoverySingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: PasswordRecoveryIntent) {
        when (intent) {
            is PasswordRecoveryIntent.UpdateEmail -> {
                _state.update { it.copy(email = intent.email) }
            }

            is PasswordRecoveryIntent.SubmitRecovery ->
                viewModelScope.launch {
                    when {
                        !_state.value.email.isValidEmail -> {
                            _singleEvent.emit(
                                PasswordRecoverySingleEvent
                                    .ShowError(
                                        message =
                                            StringResourceId(
                                                R.string.error_invalid_email,
                                            ),
                                    ),
                            )
                            _state.update { it.copy(isLoading = false) }
                        }

                        else -> {
                            sendRecoveryMailJob?.cancel()
                            sendRecoveryMailJob = launchSendRecoveryMail()
                        }
                    }
                }

            PasswordRecoveryIntent.NavigateToSignInScreen -> Unit
        }
    }

    private fun launchSendRecoveryMail() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            sendRecoveryMailUseCase(email = _state.value.email)
                .onEach { result ->
                    result
                        .onSuccess {
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(
                                PasswordRecoverySingleEvent
                                    .ShowInfo(
                                        message =
                                            StringResourceId(R.string.recovery_email_sent),
                                    ),
                            )
                        }.onError { error ->
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(
                                PasswordRecoverySingleEvent
                                    .ShowError(message = error.toUiText()),
                            )
                        }
                }.launchIn(viewModelScope)
        }
}
