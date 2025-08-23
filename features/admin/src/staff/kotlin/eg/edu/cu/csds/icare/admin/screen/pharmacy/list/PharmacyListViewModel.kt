package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.admin.screen.pharmacy.list.PharmacyListEffect.NavigateToPharmacyDetails
import eg.edu.cu.csds.icare.admin.screen.pharmacy.list.PharmacyListEffect.UpdateFabExpanded
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.pharmacy.ListPharmaciesUseCase
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
class PharmacyListViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listPharmaciesUseCase: ListPharmaciesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PharmacyListState())
    val uiState =
        _uiState
            .onStart {
                fetchPharmacies()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _uiState.value,
            )
    private val _singleEvent = MutableSharedFlow<PharmacyListEffect>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: PharmacyListEvent) {
        when (intent) {
            is PharmacyListEvent.Refresh -> {
                fetchPharmacies(forceUpdate = true)
            }

            is PharmacyListEvent.SelectPharmacy -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        NavigateToPharmacyDetails(pharmacy = intent.pharmacy),
                    )
                }
            }

            is PharmacyListEvent.UpdateFabExpanded -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        UpdateFabExpanded(isExpanded = intent.isExpanded),
                    )
                }
            }

            PharmacyListEvent.ConsumeEffect -> consumeEffect()
        }
    }

    private fun fetchPharmacies(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            listPharmaciesUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { pharmacies ->
                            _uiState.update { it.copy(pharmacies = pharmacies, isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(PharmacyListEffect.ShowError(message = error.toUiText()))
                            _uiState.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }

    private fun consumeEffect() {
        _uiState.update { it.copy(effect = null) }
    }
}
