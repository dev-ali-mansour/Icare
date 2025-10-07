package eg.edu.cu.csds.icare.feature.admin.screen.clinic

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedClinicViewModel : ViewModel() {
    private val _selectedClinic = MutableStateFlow<Clinic?>(null)
    val selectedClinic = _selectedClinic.asStateFlow()

    fun onSelectClinic(clinic: Clinic?) {
        _selectedClinic.value = clinic
    }
}
