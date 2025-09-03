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
    private val _uiState =
        MutableStateFlow(ProfileState())
    val uiState =
        _uiState
            .onStart {
                observeCurrentUser(false)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )

    val effect = _uiState.map { it.effect }

    fun processEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.UpdateGoogleSignInToken -> {
                _uiState.update { it.copy(googleToken = event.token) }
            }

            is ProfileEvent.LinkWithGoogle -> {
                linkGoogleJob?.cancel()
                linkGoogleJob = launchGoogleLinking()
            }

            is ProfileEvent.UnlinkWithGoogle -> {
                unlinkGoogleJob?.cancel()
                unlinkGoogleJob = launchGoogleUnlinking()
            }

            is ProfileEvent.SignOut -> {
                signOutJob?.cancel()
                signOutJob = launchSignOut()
            }

            is ProfileEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun observeCurrentUser(forceUpdate: Boolean) {
        observeCurrentUserJob?.cancel()
        observeCurrentUserJob =
            getUserInfoUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { user ->
                            _uiState.update { it.copy(currentUser = user) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ProfileEffect.ShowError(error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
    }

    private fun launchGoogleLinking() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            linkGoogleAccountUseCase(_uiState.value.googleToken)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update { it.copy(isLoading = false) }
                            observeCurrentUser(forceUpdate = true)
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ProfileEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun launchGoogleUnlinking() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            unlinkGoogleAccountUseCase()
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update { it.copy(isLoading = false) }
                            observeCurrentUser(forceUpdate = true)
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ProfileEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun launchSignOut() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            signOutUseCase()
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ProfileEffect.SignOutSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = ProfileEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
