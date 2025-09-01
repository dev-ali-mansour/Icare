package eg.edu.cu.csds.icare.admin.screen.doctor

@KoinViewModel
class SelectedDoctorViewModel : ViewModel() {
    private val _selectedDoctor = MutableStateFlow<Doctor?>(null)
    val selectedDoctor = _selectedDoctor.asStateFlow()

    fun onSelectDoctor(doctor: Doctor?) {
        _selectedDoctor.value = doctor
    }
}
