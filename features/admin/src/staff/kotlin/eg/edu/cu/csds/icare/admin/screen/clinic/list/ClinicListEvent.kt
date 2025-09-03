package eg.edu.cu.csds.icare.admin.screen.clinic.list

import eg.edu.cu.csds.icare.core.domain.model.Clinic

sealed interface ClinicListEvent {
    object Refresh : ClinicListEvent

    data class SelectClinic(
        val clinic: Clinic,
    ) : ClinicListEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicListEvent

    object ConsumeEffect : ClinicListEvent
}
