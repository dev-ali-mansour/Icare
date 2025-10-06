package eg.edu.cu.csds.icare.feature.admin.screen.clinician

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedClinicianViewModel : ViewModel() {
    private val _selectedClinician = MutableStateFlow<Clinician?>(null)
    val selectedClinician = _selectedClinician.asStateFlow()

    fun onSelectClinician(clinician: Clinician?) {
        _selectedClinician.value = clinician
    }
}
