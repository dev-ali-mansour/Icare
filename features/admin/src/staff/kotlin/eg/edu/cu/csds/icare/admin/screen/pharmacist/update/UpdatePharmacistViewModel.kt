package eg.edu.cu.csds.icare.admin.screen.pharmacist.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.R
import eg.edu.cu.csds.icare.admin.screen.pharmacist.PharmacistEffect
import eg.edu.cu.csds.icare.admin.screen.pharmacist.PharmacistEvent
import eg.edu.cu.csds.icare.admin.screen.pharmacist.PharmacistState
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.UpdatePharmacistUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
import eg.edu.cu.csds.icare.core.domain.util.Constants
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class UpdatePharmacistViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
    private val updatePharmacistUseCase: UpdatePharmacistUseCase,
) : ViewModel() {
    private var updatePharmacistJob: Job? = null
    private val _uiState = MutableStateFlow(PharmacistState())
    val uiState =
        _uiState
            .onStart {
                fetchPharmacies()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(intent: PharmacistEvent) {
        when (intent) {
            is PharmacistEvent.UpdateFirstName ->
                _uiState.update { it.copy(firstName = intent.firstName) }

            is PharmacistEvent.UpdateLastName ->
                _uiState.update { it.copy(lastName = intent.lastName) }

            is PharmacistEvent.UpdatePharmacyId ->
                _uiState.update { it.copy(pharmacyId = intent.pharmacyId) }

            is PharmacistEvent.UpdatePharmaciesExpanded -> {
                _uiState.update { it.copy(isPharmaciesExpanded = intent.isExpanded) }
            }

            is PharmacistEvent.UpdateEmail ->
                _uiState.update { it.copy(email = intent.email) }

            is PharmacistEvent.UpdatePhone ->
                _uiState.update { it.copy(phone = intent.phone) }

            is PharmacistEvent.UpdateProfilePicture ->
                _uiState.update { it.copy(profilePicture = intent.profilePicture) }

            is PharmacistEvent.SelectPharmacist -> {
                _uiState.update {
                    it.copy(
                        id = intent.pharmacist.id,
                        firstName = intent.pharmacist.firstName,
                        lastName = intent.pharmacist.lastName,
                        pharmacyId = intent.pharmacist.pharmacyId,
                        email = intent.pharmacist.email,
                        phone = intent.pharmacist.phone,
                        profilePicture = intent.pharmacist.profilePicture,
                    )
                }
            }

            is PharmacistEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.firstName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PharmacistEffect.ShowError(
                                            message = StringResourceId(R.string.error_first_name),
                                        ),
                                )
                            }
                        }

                        _uiState.value.lastName.isBlank() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PharmacistEffect.ShowError(
                                            message = StringResourceId(R.string.error_last_name),
                                        ),
                                )
                            }
                        }

                        _uiState.value.pharmacyId == 0.toLong() -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PharmacistEffect.ShowError(
                                            message = StringResourceId(R.string.error_pharmacy),
                                        ),
                                )
                            }
                        }

                        !_uiState.value.email.isValidEmail -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        PharmacistEffect.ShowError(
                                            message = StringResourceId(R.string.error_invalid_email),
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
                                        PharmacistEffect.ShowError(
                                            message = StringResourceId(R.string.error_phone),
                                        ),
                                )
                            }
                        }

                        else -> {
                            updatePharmacistJob?.cancel()
                            updatePharmacistJob = launchUpdateClinician()
                        }
                    }
                }

            PharmacistEvent.ConsumeEffect -> consumeEffect()
        }
    }

    private fun launchUpdateClinician() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            val pharmacist =
                Pharmacist(
                    id = _uiState.value.id,
                    firstName = _uiState.value.firstName,
                    lastName = _uiState.value.lastName,
                    pharmacyId = _uiState.value.pharmacyId,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    profilePicture = _uiState.value.profilePicture,
                )
            updatePharmacistUseCase(pharmacist)
                .onEach { result ->
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = PharmacistEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = PharmacistEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun fetchPharmacies() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listPharmaciesUseCase()
                .onEach { result ->
                    result
                        .onSuccess { pharmacies ->
                            _uiState.update { it.copy(pharmacies = pharmacies, isLoading = false) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = PharmacistEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun consumeEffect() {
        _uiState.update { it.copy(effect = null) }
    }
}
