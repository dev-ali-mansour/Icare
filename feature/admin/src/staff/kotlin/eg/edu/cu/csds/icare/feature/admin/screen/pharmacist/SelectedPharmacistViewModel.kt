package eg.edu.cu.csds.icare.feature.admin.screen.pharmacist

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedPharmacistViewModel : ViewModel() {
    private val _selectedPharmacist = MutableStateFlow<Pharmacist?>(null)
    val selectedPharmacist = _selectedPharmacist.asStateFlow()

    fun onSelectPharmacist(pharmacist: Pharmacist?) {
        _selectedPharmacist.value = pharmacist
    }
}
