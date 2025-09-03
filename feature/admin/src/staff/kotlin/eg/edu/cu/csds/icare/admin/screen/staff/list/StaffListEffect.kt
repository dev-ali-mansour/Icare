package eg.edu.cu.csds.icare.admin.screen.staff.list

import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface StaffListEffect {
    data class NavigateToStaffDetails(
        val staff: Staff,
    ) : StaffListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : StaffListEffect

    data class ShowError(
        val message: UiText,
    ) : StaffListEffect
}
