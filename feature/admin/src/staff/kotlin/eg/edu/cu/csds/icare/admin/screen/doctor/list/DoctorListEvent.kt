package eg.edu.cu.csds.icare.admin.screen.doctor.list

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface DoctorListEvent {
    object Refresh : DoctorListEvent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : DoctorListEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : DoctorListEvent

    object ConsumeEffect : DoctorListEvent
}
