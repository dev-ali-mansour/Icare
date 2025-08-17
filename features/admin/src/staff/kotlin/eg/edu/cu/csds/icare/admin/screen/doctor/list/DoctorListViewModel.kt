package eg.edu.cu.csds.icare.admin.screen.doctor.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
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
    private val _state = MutableStateFlow(DoctorListState())
    val state =
        _state
            .onStart {
                fetchDoctors()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<DoctorListSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: DoctorListIntent) {
        when (intent) {
            is DoctorListIntent.Refresh -> {
                fetchDoctors(forceUpdate = true)
            }

            is DoctorListIntent.SelectDoctor -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        DoctorListSingleEvent
                            .NavigateToDoctorDetails(doctor = intent.doctor),
                    )
                }
            }

            is DoctorListIntent.UpdateFabExpanded -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        DoctorListSingleEvent
                            .UpdateFabExpanded(isExpanded = intent.isExpanded),
                    )
                }
            }
        }
    }

    private fun fetchDoctors(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            listDoctorsUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { doctors ->
                            _state.update { it.copy(doctors = doctors, isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(DoctorListSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
