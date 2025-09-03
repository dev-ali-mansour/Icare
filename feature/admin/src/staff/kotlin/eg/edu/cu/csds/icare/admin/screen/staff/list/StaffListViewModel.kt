package eg.edu.cu.csds.icare.admin.screen.staff.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.staff.ListStaffUseCase
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
class StaffListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listStaffUseCase: ListStaffUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StaffListState())
    val uiState =
        _uiState
            .onStart {
                fetchPharmacists()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    val effect = uiState.map { it.effect }

    fun processEvent(event: StaffListEvent) {
        when (event) {
            is StaffListEvent.Refresh -> fetchPharmacists()

            is StaffListEvent.SelectStaff ->
                _uiState.update {
                    it.copy(
                        effect =
                            StaffListEffect.NavigateToStaffDetails(staff = event.staff),
                    )
                }

            is StaffListEvent.UpdateFabExpanded ->
                _uiState.update {
                    it.copy(
                        effect = StaffListEffect.UpdateFabExpanded(isExpanded = event.isExpanded),
                    )
                }

            StaffListEvent.ConsumeEffect -> _uiState.update { it.copy(effect = null) }
        }
    }

    private fun fetchPharmacists() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listStaffUseCase()
                .onEach { result ->
                    result
                        .onSuccess { staffList ->
                            _uiState.update { it.copy(isLoading = false, staffList = staffList) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    effect = StaffListEffect.ShowError(message = error.toUiText()),
                                    isLoading = false,
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
}
