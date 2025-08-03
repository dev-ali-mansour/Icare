package eg.edu.cu.csds.icare.auth.screen

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.usecase.auth.LinkTokenAccountUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SendRecoveryMailUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.SignOutUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.net.ConnectException

@KoinViewModel
class AuthViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val sendRecoveryMailUseCase: SendRecoveryMailUseCase,
    private val linkTokenAccountUseCase: LinkTokenAccountUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private val _recoveryResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val recoveryResFlow: StateFlow<Resource<Nothing?>> = _recoveryResFlow
    private val _linkResFlow = MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val linkResFlow: StateFlow<Resource<Nothing?>> = _linkResFlow
    private val _logoutResFlow =
        MutableStateFlow<Resource<Nothing?>>(Resource.Unspecified())
    val logoutResFlow: StateFlow<Resource<Nothing?>> = _logoutResFlow
    private val _isLoading = mutableStateOf(false)
    var isLoading: State<Boolean> = _isLoading

    var firstNameState = mutableStateOf("")
    var lastNameState = mutableStateOf("")
    var emailState = mutableStateOf("")
    var birthDateState = mutableLongStateOf(System.currentTimeMillis())
        private set
    var nationalIdState = mutableStateOf("")
    var phoneState = mutableStateOf("")
    var addressState = mutableStateOf("")
    var weightState = mutableDoubleStateOf(0.0)
    var chronicDiseasesState = mutableStateOf("")
    var currentMedicationsState = mutableStateOf("")
    var allergiesState = mutableStateOf("")
    var pastSurgeriesState = mutableStateOf("")
    var passwordState = mutableStateOf("")

    fun linkGoogleAccount(data: Intent?) {
        runCatching {
            _isLoading.value = true
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            viewModelScope.launch(dispatcher) {
                account.idToken?.let { token ->
                    linkTokenAccountUseCase(GoogleAuthProvider.PROVIDER_ID, token).collectLatest {
                        _isLoading.value = false
                        when (it) {
                            is Result.Success -> _linkResFlow.value = Resource.Success(null)
                            is Result.Error ->
                                _linkResFlow.value =
                                    Resource.Error(ConnectException())
                        }
                    }
                }
            }
        }.onFailure {
            _isLoading.value = false
            _linkResFlow.value = Resource.Error(it)
        }
    }

    fun onResetPasswordClicked() {
        viewModelScope.launch(dispatcher) {
            _isLoading.value = true
            if (_recoveryResFlow.value !is Resource.Unspecified) {
                _recoveryResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            sendRecoveryMailUseCase(emailState.value).collect {
                _isLoading.value = false
                when (it) {
                    is Result.Success -> {
                        clearData()
                        _recoveryResFlow.value = Resource.Success(null)
                    }

                    is Result.Error ->
                        _recoveryResFlow.value = Resource.Error(ConnectException())
                }
            }
        }
    }

    fun onLogOutClick() {
        clearData()
        viewModelScope.launch(dispatcher) {
            if (_logoutResFlow.value !is Resource.Unspecified) {
                _logoutResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            signOutUseCase().distinctUntilChanged().collect {
                _isLoading.value = false
                when (it) {
                    is Result.Success -> {
                        clearData()
                        _logoutResFlow.value = Resource.Success(null)
                    }

                    is Result.Error ->
                        _logoutResFlow.value = Resource.Error(ConnectException())
                }
            }
        }
    }

    fun clearData() {
        firstNameState.value = ""
        lastNameState.value = ""
        emailState.value = ""
        birthDateState.longValue = 0
        nationalIdState.value = ""
        phoneState.value = ""
        addressState.value = ""
        weightState.doubleValue = 0.0
        chronicDiseasesState.value = ""
        currentMedicationsState.value = ""
        allergiesState.value = ""
        pastSurgeriesState.value = ""
        passwordState.value = ""
    }
}
