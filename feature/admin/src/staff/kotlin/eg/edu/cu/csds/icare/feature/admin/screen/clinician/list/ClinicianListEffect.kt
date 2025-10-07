package eg.edu.cu.csds.icare.feature.admin.screen.clinician.list

import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicianListEffect {
    data class NavigateToClinicianDetails(
        val clinician: Clinician,
    ) : ClinicianListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicianListEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicianListEffect
}
