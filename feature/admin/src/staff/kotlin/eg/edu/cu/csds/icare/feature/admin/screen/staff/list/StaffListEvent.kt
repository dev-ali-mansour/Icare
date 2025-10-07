package eg.edu.cu.csds.icare.feature.admin.screen.staff.list

import eg.edu.cu.csds.icare.core.domain.model.Staff

sealed interface StaffListEvent {
    object Refresh : StaffListEvent

    data class SelectStaff(
        val staff: Staff,
    ) : StaffListEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : StaffListEvent

    object ConsumeEffect : StaffListEvent
}
