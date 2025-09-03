package eg.edu.cu.csds.icare.appointment.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.appointment.R
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.appointment.BookAppointmentUseCase
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.GetDoctorScheduleUseCase
import eg.edu.cu.csds.icare.core.ui.util.UiText.StringResourceId
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BookingViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val getDoctorScheduleUseCase: GetDoctorScheduleUseCase,
    private val bookAppointmentUseCase: BookAppointmentUseCase,
) : ViewModel() {
    private var getDoctorScheduleJob: Job? = null
    private var bookingJob: Job? = null
    private val _uiState = MutableStateFlow(BookingState())
    val uiState =
        _uiState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(event: BookingEvent) {
        when (event) {
            is BookingEvent.Refresh -> {
                _uiState.value.doctor?.let { doctor ->
                    getDoctorScheduleJob?.cancel()
                    getDoctorScheduleJob = launchGetDoctorSchedule(doctor.id)
                }
            }

            is BookingEvent.SelectDoctor -> {
                _uiState.update {
                    it.copy(doctor = event.doctor)
                }
                _uiState.value.doctor?.let { doctor ->
                    getDoctorScheduleJob?.cancel()
                    getDoctorScheduleJob = launchGetDoctorSchedule(doctor.id)
                }
            }

            is BookingEvent.UpdateSelectedSlot -> {
                _uiState.update { it.copy(selectedSlot = event.slot) }
            }

            is BookingEvent.Proceed ->
                viewModelScope.launch {
                    when {
                        _uiState.value.doctor == null ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        BookingEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_appointments_error_no_doctor,
                                                ),
                                        ),
                                )
                            }
                        _uiState.value.selectedSlot == 0L ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect =
                                        BookingEffect.ShowError(
                                            message =
                                                StringResourceId(
                                                    R.string.feature_appointments_error_selected_slot,
                                                ),
                                        ),
                                )
                            }

                        else -> {
                            bookingJob?.cancel()
                            bookingJob = launchBookingAppointment()
                        }
                    }
                }

            BookingEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun launchGetDoctorSchedule(uid: String) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getDoctorScheduleUseCase(uid).collect { result ->
                result
                    .onSuccess { schedule ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                doctorSchedule = schedule,
                                selectedSlot = 0L,
                            )
                        }
                    }.onError { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                effect = BookingEffect.ShowError(message = error.toUiText()),
                            )
                        }
                    }
            }
        }

    private fun launchBookingAppointment() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.value.doctor?.let { doctor ->
                bookAppointmentUseCase(
                    doctorId = doctor.id,
                    dateTime = _uiState.value.selectedSlot,
                ).onEach { result ->
                    result
                        .onSuccess { _ ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = BookingEffect.ShowSuccess,
                                )
                            }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = BookingEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
            }
        }
}
