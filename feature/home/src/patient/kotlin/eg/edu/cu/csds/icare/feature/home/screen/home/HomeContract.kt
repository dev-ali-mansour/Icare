package eg.edu.cu.csds.icare.feature.home.screen.home

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Promotion
import eg.edu.cu.csds.icare.core.domain.model.User
import eg.edu.cu.csds.icare.core.ui.navigation.Route
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class HomeState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val openDialog: Boolean = false,
    val myAppointments: List<Appointment> = emptyList(),
    val promotions: List<Promotion> = emptyList(),
    val topDoctors: List<Doctor> = emptyList(),
    val effect: HomeEffect? = null,
)

sealed interface HomeEffect {
    data class NavigateToRoute(
        val route: Route,
    ) : HomeEffect

    data class NavigateToDoctorDetails(
        val doctor: Doctor,
    ) : HomeEffect

    data class ShowError(
        val message: UiText,
    ) : HomeEffect
}

sealed interface HomeIntent {
    data class UpdateOpenDialog(
        val isOpen: Boolean,
    ) : HomeIntent

    object NavigateToProfileScreen : HomeIntent

    object NavigateToBookAppointmentScreen : HomeIntent

    object NavigateToPharmaciesScreen : HomeIntent

    object NavigateToLabCentersScreen : HomeIntent

    object NavigateToScanCentersScreen : HomeIntent

    object NavigateToMyAppointmentsScreen : HomeIntent

    data class NavigateToDoctorDetails(
        val doctor: Doctor,
    ) : HomeIntent

    object ConsumeEffect : HomeIntent
}
