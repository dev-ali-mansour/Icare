package eg.edu.cu.csds.icare.admin.screen.clinic.list

import eg.edu.cu.csds.icare.core.domain.model.Clinic

sealed interface ClinicListIntent {
    object Refresh : ClinicListIntent

    data class SelectClinic(
        val clinic: Clinic,
    ) : ClinicListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicListIntent
}
