package eg.edu.cu.csds.icare.admin.screen.clinic.list

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicListSingleEvent {
    data class NavigateToClinicDetails(
        val clinic: Clinic,
    ) : ClinicListSingleEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicListSingleEvent

    data class ShowError(
        val message: UiText,
    ) : ClinicListSingleEvent
}
