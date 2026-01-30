package eg.edu.cu.csds.icare.feature.admin.screen.staff.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.AddNewStaffUseCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
import eg.edu.cu.csds.icare.core.domain.util.isValidEmail
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.feature.admin.R
import eg.edu.cu.csds.icare.feature.admin.screen.staff.StaffEffect
import eg.edu.cu.csds.icare.feature.admin.screen.staff.StaffIntent
import eg.edu.cu.csds.icare.feature.admin.screen.staff.StaffState
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
class NewStaffViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listCentersUseCase: ListCentersUseCase,
    private val addNewStaffUseCase: AddNewStaffUseCase,
) : ViewModel() {
    private var addStaffJob: Job? = null
    private val _uiState = MutableStateFlow(StaffState())
    val uiState =
        _uiState
            .onStart {
                fetchCenters()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )

    val effect = uiState.map { it.effect }

    fun handleIntent(intent: StaffIntent) {
        when (intent) {
            is StaffIntent.UpdateFirstName -> {
                _uiState.update { it.copy(firstName = intent.firstName) }
            }

            is StaffIntent.UpdateLastName -> {
                _uiState.update { it.copy(lastName = intent.lastName) }
            }

            is StaffIntent.UpdateCenterId -> {
                _uiState.update { it.copy(centerId = intent.centerId) }
            }

            is StaffIntent.UpdateCentersExpanded -> {
                _uiState.update { it.copy(isCentersExpanded = intent.isExpanded) }
            }

            is StaffIntent.UpdateEmail -> {
                _uiState.update { it.copy(email = intent.email) }
            }

            is StaffIntent.UpdatePhone -> {
                _uiState.update { it.copy(phone = intent.phone) }
            }

            is StaffIntent.UpdateProfilePicture -> {
                _uiState.update { it.copy(profilePicture = intent.profilePicture) }
            }

            is StaffIntent.LoadStaff -> {
                Unit
            }

            is StaffIntent.Proceed -> {
                viewModelScope.launch {
                    when {
                        _uiState.value.firstName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        StaffEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_first_name,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.lastName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        StaffEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_last_name,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.centerId == 0.toLong() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        StaffEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_center),
                                        ),
                                )
                            }
                        }

                        !_uiState.value.email.isValidEmail -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        StaffEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_admin_error_invalid_email,
                                                ),
                                        ),
                                )
                            }
                        }

                        _uiState.value.phone.isBlank() ||
                            _uiState.value.phone.length < Constants.PHONE_LENGTH -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        StaffEffect.ShowError(
                                            message = StringResourceId(R.string.feature_admin_error_phone),
                                        ),
                                )
                            }
                        }

                        else -> {
                            addStaffJob?.cancel()
                            addStaffJob = launchAddNewPharmacist()
                        }
                    }
                }
            }

            StaffIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
        }
    }

    private fun launchAddNewPharmacist() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val staff =
                Staff(
                    firstName = _uiState.value.firstName,
                    lastName = _uiState.value.lastName,
                    centerId = _uiState.value.centerId,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    profilePicture = _uiState.value.profilePicture,
                )
            addNewStaffUseCase(staff)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = StaffEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = StaffEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun fetchCenters() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listCentersUseCase()
                .onEach { result ->
                    result
                        .onSuccess { centers ->
                            _uiState.update { it.copy(isLoading = false, centers = centers) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = StaffEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
