package eg.edu.cu.csds.icare.admin.screen.clinician.list

import eg.edu.cu.csds.icare.core.domain.model.Clinician

sealed interface ClinicianListIntent {
    object Refresh : ClinicianListIntent

    data class SelectClinician(
        val clinician: Clinician,
    ) : ClinicianListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicianListIntent
}
