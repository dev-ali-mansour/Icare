package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _state = MutableStateFlow(PharmacyListState())
    val state =
        _state
            .onStart {
                fetchPharmacies()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = _state.value,
            )
    private val _singleEvent = MutableSharedFlow<PharmacyListSingleEvent>()
    val singleEvent = _singleEvent.asSharedFlow()

    fun processIntent(intent: PharmacyListIntent) {
        when (intent) {
            is PharmacyListIntent.Refresh -> {
                fetchPharmacies(forceUpdate = true)
            }

            is PharmacyListIntent.SelectPharmacy -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        PharmacyListSingleEvent
                            .NavigateToPharmacyDetails(pharmacy = intent.pharmacy),
                    )
                }
            }

            is PharmacyListIntent.UpdateFabExpanded -> {
                viewModelScope.launch {
                    _singleEvent.emit(
                        PharmacyListSingleEvent
                            .UpdateFabExpanded(isExpanded = intent.isExpanded),
                    )
                }
            }
        }
    }

    private fun fetchPharmacies(forceUpdate: Boolean = false) =
        viewModelScope.launch(dispatcher) {
            _state.update { it.copy(isLoading = true) }
            listPharmaciesUseCase(forceUpdate = forceUpdate)
                .onEach { result ->
                    result
                        .onSuccess { pharmacies ->
                            _state.update { it.copy(pharmacies = pharmacies, isLoading = false) }
                        }.onError { error ->
                            _singleEvent.emit(PharmacyListSingleEvent.ShowError(message = error.toUiText()))
                            _state.update { it.copy(isLoading = false) }
                        }
                }.launchIn(viewModelScope)
        }
}
