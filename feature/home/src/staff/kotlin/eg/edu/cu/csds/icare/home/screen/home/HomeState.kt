package eg.edu.cu.csds.icare.home.screen.home

import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.User

data class HomeState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val openDialog: Boolean = false,
    val adminStatistics: AdminStatistics? = null,
    val currentDoctor: Doctor? = null,
    val doctorSchedule: DoctorSchedule? = null,
    val appointments: List<Appointment> = emptyList(),
    val effect: HomeEffect? = null,
)
