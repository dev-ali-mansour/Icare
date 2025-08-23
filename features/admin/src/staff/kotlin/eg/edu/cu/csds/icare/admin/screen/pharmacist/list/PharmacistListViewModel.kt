package eg.edu.cu.csds.icare.admin.screen.pharmacist.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.screen.pharmacist.list.PharmacistListEffect.NavigateToPharmacistDetails
import eg.edu.cu.csds.icare.admin.screen.pharmacist.list.PharmacistListEffect.ShowError
import eg.edu.cu.csds.icare.admin.screen.pharmacist.list.PharmacistListEffect.UpdateFabExpanded
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacist.ListPharmacistsUseCase
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
class PharmacistListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listPharmacistsUseCase: ListPharmacistsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PharmacistListState())
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

    fun processEvent(event: PharmacistListEvent) {
        when (event) {
            is PharmacistListEvent.Refresh -> {
                fetchPharmacists()
            }

            is PharmacistListEvent.SelectPharmacist -> {
                _uiState.update {
                    it.copy(
                        effect =
                            NavigateToPharmacistDetails(pharmacist = event.pharmacist),
                    )
                }
            }

            is PharmacistListEvent.UpdateFabExpanded -> {
                _uiState.update {
                    it.copy(
                        effect = UpdateFabExpanded(isExpanded = event.isExpanded),
                    )
                }
            }

            PharmacistListEvent.ConsumeEffect -> consumeEffect()
        }
    }

    private fun fetchPharmacists() =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listPharmacistsUseCase()
                .onEach { result ->
                    result
                        .onSuccess { pharmacists ->
                            _uiState.update { it.copy(pharmacists = pharmacists, isLoading = false) }
                        }.onError { error ->
                            _uiState.update {
                                it.copy(
                                    effect = ShowError(message = error.toUiText()),
                                    isLoading = false,
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }

    private fun consumeEffect() {
        _uiState.update { it.copy(effect = null) }
    }
}
