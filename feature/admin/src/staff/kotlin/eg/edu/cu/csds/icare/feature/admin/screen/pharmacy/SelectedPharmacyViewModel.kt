package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedPharmacyViewModel : ViewModel() {
    private val _selectedPharmacy = MutableStateFlow<Pharmacy?>(null)
    val selectedPharmacy = _selectedPharmacy.asStateFlow()

    fun onSelectPharmacy(pharmacy: Pharmacy?) {
        _selectedPharmacy.value = pharmacy
    }
}
