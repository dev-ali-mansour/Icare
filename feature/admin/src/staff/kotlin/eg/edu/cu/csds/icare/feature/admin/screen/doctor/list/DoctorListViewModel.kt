package eg.edu.cu.csds.icare.feature.admin.screen.doctor.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.doctor.ListDoctorsUseCase
import eg.edu.cu.csds.icare.core.ui.util.toUiText
import eg.edu.cu.csds.icare.feature.admin.screen.doctor.list.DoctorListEffect.NavigateToDoctorDetails
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

    fun handleIntent(intent: DoctorListIntent) {
        when (intent) {
            is DoctorListIntent.Refresh -> {
                fetchDoctors(forceUpdate = true)
            }

            is DoctorListIntent.SelectDoctor -> {
                _uiState.update {
                    it.copy(effect = NavigateToDoctorDetails(doctor = intent.doctor))
                }
            }

            is DoctorListIntent.UpdateFabExpanded -> {
                _uiState.update {
                    it.copy(
                        effect =
                            DoctorListEffect
                                .UpdateFabExpanded(isExpanded = intent.isExpanded),
                    )
                }
            }

            DoctorListIntent.ConsumeEffect -> {
                _uiState.update { it.copy(effect = null) }
            }
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
