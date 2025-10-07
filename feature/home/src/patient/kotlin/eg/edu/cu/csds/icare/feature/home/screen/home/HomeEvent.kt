package eg.edu.cu.csds.icare.feature.home.screen.home

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface HomeEvent {
    data class UpdateOpenDialog(
        val isOpen: Boolean,
    ) : HomeEvent

    object NavigateToProfileScreen : HomeEvent

    object NavigateToBookAppointmentScreen : HomeEvent

    object NavigateToPharmaciesScreen : HomeEvent

    object NavigateToLabCentersScreen : HomeEvent

    object NavigateToScanCentersScreen : HomeEvent

    object NavigateToMyAppointmentsScreen : HomeEvent

    data class NavigateToDoctorDetails(
        val doctor: Doctor,
    ) : HomeEvent

    object ConsumeEffect : HomeEvent
}
