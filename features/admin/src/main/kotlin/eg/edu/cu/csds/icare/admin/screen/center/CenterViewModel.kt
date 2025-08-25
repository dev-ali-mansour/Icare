package eg.edu.cu.csds.icare.admin.screen.center

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.usecase.center.ListCentersUseCase
import eg.edu.cu.csds.icare.core.ui.common.CenterTypeItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CenterViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val listCentersUseCase: ListCentersUseCase,
) : ViewModel() {
    private val _centersResFlow =
        MutableStateFlow<Resource<List<LabImagingCenter>>>(Resource.Unspecified())
    val centersResFlow: StateFlow<Resource<List<LabImagingCenter>>> = _centersResFlow

    var isRefreshing = mutableStateOf(false)
    var searchQueryState = mutableStateOf("")

    fun listLabCenters(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            searchQueryState.value = ""
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase(forceUpdate = forceRefresh).collect { result ->
                result
                    .onSuccess { centers ->
                        val filtered =
                            centers.filter { center ->
                                center.type == CenterTypeItem.LabCenter.code
                            }
                        _centersResFlow.value = Resource.Success(filtered)
                    }.onError {
                        _centersResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }

    fun searchLabCenters() {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase().collect { result ->
                result
                    .onSuccess { centers ->
                        val filtered =
                            centers.filter { center ->
                                center.type == CenterTypeItem.LabCenter.code &&
                                    (
                                        searchQueryState.value.isEmpty() ||
                                            center.name.contains(
                                                searchQueryState.value,
                                                ignoreCase = true,
                                            ) ||
                                            center.address.contains(
                                                searchQueryState.value,
                                                ignoreCase = true,
                                            )
                                    )
                            }
                        _centersResFlow.value = Resource.Success(filtered)
                    }.onError {
                        _centersResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }

    fun listImagingCenters(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            searchQueryState.value = ""
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase(forceUpdate = forceRefresh).collect { result ->
                result
                    .onSuccess { centers ->
                        val filtered =
                            centers.filter { center ->
                                center.type == CenterTypeItem.ImagingCenter.code
                            }
                        _centersResFlow.value = Resource.Success(filtered)
                    }.onError {
                        _centersResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }

    fun searchImagingCenters() {
        viewModelScope.launch(dispatcher) {
            if (_centersResFlow.value !is Resource.Unspecified) {
                _centersResFlow.value = Resource.Unspecified()
                delay(timeMillis = 100)
            }
            listCentersUseCase().collect { result ->
                result
                    .onSuccess { centers ->
                        val filtered =
                            centers.filter { center ->
                                center.type == CenterTypeItem.ImagingCenter.code &&
                                    (
                                        searchQueryState.value.isEmpty() ||
                                            center.name.contains(
                                                searchQueryState.value,
                                                ignoreCase = true,
                                            ) ||
                                            center.address.contains(
                                                searchQueryState.value,
                                                ignoreCase = true,
                                            )
                                    )
                            }
                        _centersResFlow.value = Resource.Success(filtered)
                    }.onError {
                        _centersResFlow.value = Resource.Error(Exception(it.toString()))
                    }
            }
        }
    }
}
