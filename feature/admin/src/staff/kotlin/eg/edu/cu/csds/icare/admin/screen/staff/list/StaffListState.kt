package eg.edu.cu.csds.icare.admin.screen.staff.list

import eg.edu.cu.csds.icare.core.domain.model.Staff

data class StaffListState(
    val isLoading: Boolean = false,
    val staffList: List<Staff> = emptyList(),
    val effect: StaffListEffect? = null,
)
