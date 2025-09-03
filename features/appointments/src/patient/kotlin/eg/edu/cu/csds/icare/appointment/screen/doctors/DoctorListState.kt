package eg.edu.cu.csds.icare.appointment.screen.doctors

import eg.edu.cu.csds.icare.core.domain.model.Doctor

data class DoctorListState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val doctors: List<Doctor> = emptyList(),
    val effect: DoctorListEffect? = null,
)
