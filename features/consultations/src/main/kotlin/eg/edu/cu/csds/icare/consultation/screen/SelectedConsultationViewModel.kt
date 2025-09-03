package eg.edu.cu.csds.icare.consultation.screen

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedConsultationViewModel : ViewModel() {
    private val _selectedConsultation = MutableStateFlow<Consultation?>(null)
    val selectedConsultation = _selectedConsultation.asStateFlow()

    fun onSelectConsultation(consultation: Consultation?) {
        _selectedConsultation.value = consultation
    }
}
