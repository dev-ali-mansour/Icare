package eg.edu.cu.csds.icare.appointment.screen

import androidx.lifecycle.ViewModel
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SelectedAppointmentViewModel : ViewModel() {
    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment = _selectedAppointment.asStateFlow()

    fun onSelectAppointment(appointment: Appointment?) {
        _selectedAppointment.value = appointment
    }
}
