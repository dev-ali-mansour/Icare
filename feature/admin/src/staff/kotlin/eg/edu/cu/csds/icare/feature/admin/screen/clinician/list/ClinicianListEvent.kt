package eg.edu.cu.csds.icare.feature.admin.screen.clinician.list

import eg.edu.cu.csds.icare.core.domain.model.Clinician

sealed interface ClinicianListEvent {
    object Refresh : ClinicianListEvent

    data class SelectClinician(
        val clinician: Clinician,
    ) : ClinicianListEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicianListEvent

    object ConsumeEffect : ClinicianListEvent
}
