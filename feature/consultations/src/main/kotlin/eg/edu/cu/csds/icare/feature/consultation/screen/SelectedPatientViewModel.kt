package eg.edu.cu.csds.icare.feature.consultation.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedPatientViewModel : ViewModel() {
    private val _selectedPatientId = MutableStateFlow<String?>(null)
    val selectedPatientId = _selectedPatientId.asStateFlow()

    fun onSelectPatientId(patientId: String?) {
        _selectedPatientId.value = patientId
    }
}
