package eg.edu.cu.csds.icare.admin.screen.staff

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Staff
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedStaffViewModel : ViewModel() {
    private val _selectedStaff = MutableStateFlow<Staff?>(null)
    val selectedStaff = _selectedStaff.asStateFlow()

    fun onSelectStaff(staff: Staff?) {
        _selectedStaff.value = staff
    }
}
