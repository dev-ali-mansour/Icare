package eg.edu.cu.csds.icare.auth.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.LinkGoogleAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOutUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.UnlinkGoogleAccountUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val linkGoogleAccountUseCase: LinkGoogleAccountUseCase,
    private val unlinkGoogleAccountUseCase: UnlinkGoogleAccountUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private var observeCurrentUserJob: Job? = null
    private var linkGoogleJob: Job? = null
    private var unlinkGoogleJob: Job? = null
    private var signOutJob: Job? = null
    private val _state =
        MutableStateFlow(ProfileState())
    val state =
        _state
            .onStart {
                observeCurrentUser(false)
            }.stateIn(
                scope = viewModelScope,
                started =
                    kotlinx.coroutines.flow.SharingStarted
                        .WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent =
        MutableSharedFlow<ProfileSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.UpdateGoogleSignInToken -> {
                _state.update { it.copy(googleToken = intent.token) }
            }
            is ProfileIntent.LinkWithGoogle -> {
                linkGoogleJob?.cancel()
                linkGoogleJob = launchGoogleLinking()
            }
            is ProfileIntent.UnlinkWithGoogle -> {
                unlinkGoogleJob?.cancel()
                unlinkGoogleJob = launchGoogleUnlinking()
            }
            is ProfileIntent.SignOut -> {
                signOutJob?.cancel()
                signOutJob = launchSignOut()
            }
        }
    }

    private fun observeCurrentUser(forceUpdate: Boolean) {
        observeCurrentUserJob?.cancel()
        observeCurrentUserJob =
            getUserInfoUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { user ->
                            _state.update { it.copy(currentUser = user) }
                        }.onError { error ->
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(ProfileSingleEvent.ShowError(error.toUiText()))
                        }
                }.launchIn(viewModelScope)
    }

    private fun launchGoogleLinking() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            linkGoogleAccountUseCase(_state.value.googleToken)
                .onEach { result ->
                    result
                        .onSuccess {
                            _state.update { it.copy(isLoading = false) }
                            observeCurrentUser(forceUpdate = true)
                        }.onError { error ->
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(ProfileSingleEvent.ShowError(message = error.toUiText()))
                        }
                }.launchIn(viewModelScope)
        }

    private fun launchGoogleUnlinking() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            unlinkGoogleAccountUseCase()
                .onEach { result ->
                    result
                        .onSuccess {
                            _state.update { it.copy(isLoading = false) }
                            observeCurrentUser(forceUpdate = true)
                        }.onError { error ->
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(ProfileSingleEvent.ShowError(message = error.toUiText()))
                        }
                }.launchIn(viewModelScope)
        }

    private fun launchSignOut() =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            signOutUseCase()
                .onEach { result ->
                    result
                        .onSuccess {
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(ProfileSingleEvent.SignOutSuccess)
                        }.onError { error ->
                            _state.update { it.copy(isLoading = false) }
                            _singleEvent.emit(
                                ProfileSingleEvent.ShowError(message = error.toUiText()),
                            )
                        }
                }.launchIn(viewModelScope)
        }
}
