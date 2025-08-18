package eg.edu.cu.csds.icare.admin.screen.clinician.list

import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicianListSingleEvent {
    data class NavigateToClinicianDetails(
        val clinician: Clinician,
    ) : ClinicianListSingleEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicianListSingleEvent

    data class ShowError(
        val message: UiText,
    ) : ClinicianListSingleEvent
}
