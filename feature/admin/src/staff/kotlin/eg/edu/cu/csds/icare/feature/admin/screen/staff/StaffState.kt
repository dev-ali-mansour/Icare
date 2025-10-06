package eg.edu.cu.csds.icare.feature.admin.screen.staff

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

data class StaffState(
    val isLoading: Boolean = false,
    val centers: List<LabImagingCenter> = emptyList(),
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "$firstName $lastName",
    val centerId: Long = 0,
    val isCentersExpanded: Boolean = false,
    val centerName: String = "",
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
    val effect: StaffEffect? = null,
)
