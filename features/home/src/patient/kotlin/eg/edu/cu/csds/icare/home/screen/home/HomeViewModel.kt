package eg.edu.cu.csds.icare.home.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.Promotion
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.GetPatientAppointmentsUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.auth.GetUserInfoUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListTopDoctorsUseCase
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getPatientAppointmentUseCase: GetPatientAppointmentsUseCase,
    private val listTopDoctorsUseCase: ListTopDoctorsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState =
        _uiState
            .onStart {
                getCurrentUser()
                getPatientAppointments()
                getPromotions()
                getTopDoctors()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = _uiState.map { it.effect }

    fun processEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.UpdateOpenDialog -> _uiState.update { it.copy(openDialog = event.isOpen) }

            HomeEvent.NavigateToProfileScreen ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToRoute(route = Route.Profile))
                }

            is HomeEvent.NavigateToBookAppointmentScreen ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToRoute(route = Route.DoctorList))
                }

            HomeEvent.NavigateToPharmaciesScreen ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToRoute(route = Route.Pharmacies))
                }

            HomeEvent.NavigateToLabCentersScreen ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToRoute(route = Route.LabCenters))
                }

            HomeEvent.NavigateToScanCentersScreen ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToRoute(route = Route.ScanCenters))
                }

            HomeEvent.NavigateToMyAppointmentsScreen ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToRoute(route = Route.MyAppointments))
                }

            is HomeEvent.NavigateToDoctorDetails ->
                _uiState.update {
                    it.copy(effect = HomeEffect.NavigateToDoctorDetails(doctor = event.doctor))
                }

            is HomeEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun getPatientAppointments() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getPatientAppointmentUseCase().collect { result ->
                result
                    .onSuccess { appointments ->
                        _uiState.update { it.copy(isLoading = false, myAppointments = appointments) }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = HomeEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }

    private fun getCurrentUser() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase(forceUpdate = false).collect { result ->
                result
                    .onSuccess { user ->
                        _uiState.update {
                            it.copy(isLoading = false, currentUser = user)
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(effect = HomeEffect.ShowError(message = error.toUiText()))
                        }
                    }
            }
        }

    private fun getPromotions() =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    promotions =
                        listOf(
                            Promotion(
                                id = 1,
                                imageUrl = "https://i.postimg.cc/5jjyk7Jn/promo1.png",
                                discount = "30%",
                            ),
                            Promotion(
                                id = 2,
                                imageUrl = "https://i.postimg.cc/vDjTRrHM/promo2.png",
                                discount = "50%",
                            ),
                        ),
                )
            }
        }

    private fun getTopDoctors() =
        viewModelScope.launch(dispatcher) {
            listTopDoctorsUseCase().collect { result ->
                result
                    .onSuccess { doctors ->
                        _uiState.update {
                            it.copy(topDoctors = doctors)
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(effect = HomeEffect.ShowError(message = error.toUiText()))
                        }
                    }
            }
        }
}
