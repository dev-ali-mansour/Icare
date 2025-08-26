package eg.edu.cu.csds.icare.admin.screen.clinic.list

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicListEffect {
    data class NavigateToClinicDetails(
        val clinic: Clinic,
    ) : ClinicListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicListEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicListEffect
}
