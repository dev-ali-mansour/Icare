package eg.edu.cu.csds.icare.home.screen.home

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Promotion
import eg.edu.cu.csds.icare.core.domain.model.User

data class HomeState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val openDialog: Boolean = false,
    val myAppointments: List<Appointment> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val topDoctors: List<Doctor> = emptyList(),
    val effect: HomeEffect? = null,
)
