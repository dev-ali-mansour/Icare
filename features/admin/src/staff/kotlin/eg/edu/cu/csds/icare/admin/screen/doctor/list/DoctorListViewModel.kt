package eg.edu.cu.csds.icare.admin.screen.doctor.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.screen.doctor.list.DoctorListEffect.NavigateToDoctorDetails
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
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
class DoctorListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listDoctorsUseCase: ListDoctorsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DoctorListState())
    val uiState =
        _uiState
            .onStart {
                fetchDoctors()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(event: DoctorLisEvent) {
        when (event) {
            is DoctorLisEvent.Refresh -> {
                fetchDoctors(forceUpdate = true)
            }

            is DoctorLisEvent.SelectDoctor -> {
                _uiState.update {
                    it.copy(effect = NavigateToDoctorDetails(doctor = event.doctor))
                }
            }

            is DoctorLisEvent.UpdateFabExpanded -> {
                _uiState.update {
                    it.copy(
                        effect =
                            DoctorListEffect
                                .UpdateFabExpanded(isExpanded = event.isExpanded),
                    )
                }
            }

            DoctorLisEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun fetchDoctors(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listDoctorsUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { doctors ->
                            _uiState.update { it.copy(isLoading = false, doctors = doctors) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    effect = DoctorListEffect.ShowError(message = error.toUiText()),
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
