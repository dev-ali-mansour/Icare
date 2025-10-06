package eg.edu.cu.csds.icare.feature.admin.screen.doctor

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedDoctorViewModel : ViewModel() {
    private val _selectedDoctor = MutableStateFlow<Doctor?>(null)
    val selectedDoctor = _selectedDoctor.asStateFlow()

    fun onSelectDoctor(doctor: Doctor?) {
        _selectedDoctor.value = doctor
    }
}
