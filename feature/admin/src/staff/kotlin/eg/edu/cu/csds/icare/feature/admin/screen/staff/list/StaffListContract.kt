package eg.edu.cu.csds.icare.feature.admin.screen.staff.list

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class StaffListState(
    val isLoading: Boolean = false,
    val staffList: List<Staff> = emptyList(),
    val effect: StaffListEffect? = null,
)

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

sealed interface StaffListIntent {
    object Refresh : StaffListIntent

    data class SelectStaff(
        val staff: Staff,
    ) : StaffListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : StaffListIntent

    object ConsumeEffect : StaffListIntent
}
