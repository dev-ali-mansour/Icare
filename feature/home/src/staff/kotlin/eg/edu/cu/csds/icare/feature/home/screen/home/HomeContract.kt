package eg.edu.cu.csds.icare.feature.home.screen.home

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
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

sealed interface HomeEffect {
    data class NavigateToRoute(
        val route: Route,
    ) : HomeEffect

    data class NavigateToUpdateDoctorScreen(
        val doctor: Doctor,
    ) : HomeEffect

    data class NavigateToNewConsultation(
        val appointment: Appointment,
    ) : HomeEffect

    data class ShowError(
        val message: UiText,
    ) : HomeEffect
}

sealed interface HomeIntent {
    object NavigateToProfileScreen : HomeIntent

    object Refresh : HomeIntent

    data class UpdateOpenDialog(
        val isOpen: Boolean,
    ) : HomeIntent

    object NavigateToAppointmentsScreen : HomeIntent

    data class ConfirmAppointment(
        val appointment: Appointment,
    ) : HomeIntent

    data class NavigateToUpdateDoctorScreen(
        val doctor: Doctor,
    ) : HomeIntent

    data class NavigateToNewConsultation(
        val appointment: Appointment,
    ) : HomeIntent

    object NavigateToSectionsAdminScreen : HomeIntent

    object ConsumeEffect : HomeIntent
}
