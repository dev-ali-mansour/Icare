package eg.edu.cu.csds.icare.auth.screen

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.usecase.auth.DeleteAccount
import eg.edu.cu.csds.icare.core.domain.usecase.auth.LinkTokenAccount
import eg.edu.cu.csds.icare.core.domain.usecase.auth.Register
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMail
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithEmailAndPassword
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignInWithGoogle
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOut
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AuthViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val register: Register,
    private val signInWithEmailAndPassword: SignInWithEmailAndPassword,
    private val signInWithFacebook: SignInWithGoogle,
    private val sendRecoveryMail: SendRecoveryMail,
    private val linkTokenAccount: LinkTokenAccount,
    private val signOut: SignOut,
    private val deleteAccount: DeleteAccount,
) : ViewModel() {
    private val _registerResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val registerResFlow: StateFlow<Resource<Nothing?>> = _registerResFlow
    private val _loginResFlow =
        MutableStateFlow<Resource<Boolean>>(Resource.Unspecified())
    val loginResFlow: StateFlow<Resource<Boolean>> = _loginResFlow
    private val _recoveryResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val recoveryResFlow: StateFlow<Resource<Nothing?>> = _recoveryResFlow
    private val _linkResFlow = MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val linkResFlow: StateFlow<Resource<Nothing?>> = _linkResFlow
    private val _logoutResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val logoutResFlow: StateFlow<Resource<Nothing?>> = _logoutResFlow
    private val _deleteAccountResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val deleteAccountResFlow: StateFlow<Resource<Nothing?>> = _deleteAccountResFlow
    private val _isLoading = mutableStateOf(false)
    var isLoading: State<Boolean> = _isLoading
    var branchesExpanded = mutableStateOf(false)

    var nameState = mutableStateOf("")
        private set
    var emailState = mutableStateOf("")
        private set
    var nationalIdState = mutableStateOf("")
        private set
    var phoneState = mutableStateOf("")
        private set
    var passwordState = mutableStateOf("")
        private set
    var passwordVisibility = mutableStateOf(false)

    fun onNameChanged(newValue: String) {
        nameState.value = newValue
    }

    fun onEmailChanged(newValue: String) {
        emailState.value = newValue
    }

    fun onNationalIdChanged(newValue: String) {
        nationalIdState.value = newValue
    }

    fun onPhoneChanged(newValue: String) {
        phoneState.value = newValue
    }

    fun onPasswordChanged(newValue: String) {
        passwordState.value = newValue
    }

    fun onRegisterClicked() {
        viewModelScope.launch(dispatcher) {
            if (_registerResFlow.value !is Resource.Unspecified) {
                _registerResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            register(
                name = nameState.value,
                email = emailState.value,
                nationalId = nationalIdState.value,
                phone = phoneState.value,
                password = passwordState.value,
            ).collectLatest {
                _isLoading.value = it is Resource.Loading
                _registerResFlow.value = it
            }
        }
    }

    fun onLogInClicked() {
        viewModelScope.launch(dispatcher) {
            if (_loginResFlow.value !is Resource.Unspecified) {
                _loginResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            signInWithEmailAndPassword(emailState.value, passwordState.value).collectLatest {
                _isLoading.value = it is Resource.Loading
                _loginResFlow.value = it
            }
        }
    }

    fun signInWithGoogle(data: Intent?) {
        runCatching {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            viewModelScope.launch(dispatcher) {
                account.idToken?.let { token ->
                    signInWithFacebook(token).collectLatest {
                        _isLoading.value = it is Resource.Loading
                        _loginResFlow.value = it
                    }
                }
            }
        }.onFailure { _loginResFlow.value = Resource.Error(it) }
    }

    fun linkGoogleAccount(data: Intent?) {
        runCatching {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            viewModelScope.launch(dispatcher) {
                account.idToken?.let { token ->
                    linkTokenAccount(GoogleAuthProvider.PROVIDER_ID, token).collectLatest {
                        _isLoading.value = it is Resource.Loading
                        _linkResFlow.value = it
                    }
                }
            }
        }.onFailure { _linkResFlow.value = Resource.Error(it) }
    }

    fun onResetPasswordClicked() {
        viewModelScope.launch(dispatcher) {
            if (_recoveryResFlow.value !is Resource.Unspecified) {
                _recoveryResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            sendRecoveryMail(emailState.value).collect {
                _isLoading.value = it is Resource.Loading
                _recoveryResFlow.value = it
            }
        }
    }

    fun onLogOutClick() {
        viewModelScope.launch(dispatcher) {
            if (_logoutResFlow.value !is Resource.Unspecified) {
                _logoutResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            signOut().distinctUntilChanged().collect {
                _logoutResFlow.value = it
            }
        }
    }

    fun onDeleteAccountClicked() {
        viewModelScope.launch(dispatcher) {
            if (_deleteAccountResFlow.value !is Resource.Unspecified) {
                _deleteAccountResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            deleteAccount().distinctUntilChanged().collect {
                _deleteAccountResFlow.value = it
            }
        }
    }
}
