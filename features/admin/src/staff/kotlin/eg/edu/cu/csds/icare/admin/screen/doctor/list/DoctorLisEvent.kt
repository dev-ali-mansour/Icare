package eg.edu.cu.csds.icare.admin.screen.doctor.list

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface DoctorLisEvent {
    object Refresh : DoctorLisEvent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : DoctorLisEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : DoctorLisEvent

    object ConsumeEffect : DoctorLisEvent
}
