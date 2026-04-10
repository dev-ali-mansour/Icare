package eg.edu.cu.csds.icare.feature.home.screen.home

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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

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
    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is HomeIntent.UpdateOpenDialog -> {
                    _uiState.update { it.copy(openDialog = intent.isOpen) }
                }

                HomeIntent.NavigateToProfileScreen -> {
                    _effect.send(HomeEffect.NavigateToRoute(route = Route.Profile))
                }

                is HomeIntent.NavigateToBookAppointmentScreen -> {
                    _effect.send(HomeEffect.NavigateToRoute(route = Route.DoctorList))
                }

                HomeIntent.NavigateToPharmaciesScreen -> {
                    _effect.send(HomeEffect.NavigateToRoute(route = Route.Pharmacies))
                }

                HomeIntent.NavigateToLabCentersScreen -> {
                    _effect.send(HomeEffect.NavigateToRoute(route = Route.LabCenters))
                }

                HomeIntent.NavigateToScanCentersScreen -> {
                    _effect.send(HomeEffect.NavigateToRoute(route = Route.ScanCenters))
                }

                HomeIntent.NavigateToMyAppointmentsScreen -> {
                    _effect.send(HomeEffect.NavigateToRoute(route = Route.MyAppointments))
                }

                is HomeIntent.NavigateToDoctorDetails -> {
                    _effect.send(HomeEffect.NavigateToDoctorDetails(doctor = intent.doctor))
                }
            }
        }
    }

    private fun getPatientAppointments() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getPatientAppointmentUseCase().collect { result ->
                result
                    .onSuccess { appointments ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                myAppointments = appointments.toPersistentList(),
                            )
                        }
                    }.onError { error ->
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.send(HomeEffect.ShowError(message = error.toUiText()))
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
                        _effect.send(HomeEffect.ShowError(message = error.toUiText()))
                    }
            }
        }

    private fun getPromotions() =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    promotions =
                        persistentListOf(
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
                            it.copy(topDoctors = doctors.toPersistentList())
                        }
                    }.onError { error ->
                        _effect.send(HomeEffect.ShowError(message = error.toUiText()))
                    }
            }
        }
}
