package eg.edu.cu.csds.icare.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber
import java.net.ConnectException

@KoinViewModel
class MainViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _currentUserFlow: MutableStateFlow<Resource<User>> =
        MutableStateFlow(Resource.Unspecified())
    val currentUserFlow: StateFlow<Resource<User>> = _currentUserFlow

    init {
        viewModelScope.launch(dispatcher) {
            runCatching {
                getUserInfo()
            }.onFailure {
                Timber.tag("MainViewModel").e(it.message.toString())
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch(dispatcher) {
            getUserInfoUseCase(forceUpdate = true)
                .distinctUntilChanged()
                .onEach {
                    when (it) {
                        is Result.Success -> {
                            _currentUserFlow.value = Resource.Success(it.data)
                        }

                        is Result.Error -> {
                            _currentUserFlow.value = Resource.Error(ConnectException())
                        }
                    }
                }.collect {}
        }
    }
}
