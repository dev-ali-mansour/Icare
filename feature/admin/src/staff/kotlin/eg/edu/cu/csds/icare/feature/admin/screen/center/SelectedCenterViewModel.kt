package eg.edu.cu.csds.icare.feature.admin.screen.center

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedCenterViewModel : ViewModel() {
    private val _selectedCenter = MutableStateFlow<LabImagingCenter?>(null)
    val selectedCenter = _selectedCenter.asStateFlow()

    fun onSelectCenter(center: LabImagingCenter?) {
        _selectedCenter.value = center
    }
}
