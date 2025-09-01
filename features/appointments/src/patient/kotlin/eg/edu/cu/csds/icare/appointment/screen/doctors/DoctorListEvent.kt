package eg.edu.cu.csds.icare.appointment.screen.doctors

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface DoctorListEvent {
    object OnBackClick : DoctorListEvent

    object Refresh : DoctorListEvent

    data class UpdateSearchQuery(
        val query: String,
    ) : DoctorListEvent

    object Search : DoctorListEvent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : DoctorListEvent

    object ConsumeEffect : DoctorListEvent
}
