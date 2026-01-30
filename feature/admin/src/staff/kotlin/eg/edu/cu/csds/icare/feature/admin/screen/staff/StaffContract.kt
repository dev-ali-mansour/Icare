package eg.edu.cu.csds.icare.feature.admin.screen.staff

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
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

sealed interface StaffEffect {
    object ShowSuccess : StaffEffect

    data class ShowError(
        val message: UiText,
    ) : StaffEffect
}

sealed interface StaffIntent {
    data class UpdateFirstName(
        val firstName: String,
    ) : StaffIntent

    data class UpdateLastName(
        val lastName: String,
    ) : StaffIntent

    data class UpdateCenterId(
        val centerId: Long,
    ) : StaffIntent

    data class UpdateCentersExpanded(
        val isExpanded: Boolean,
    ) : StaffIntent

    data class UpdateEmail(
        val email: String,
    ) : StaffIntent

    data class UpdatePhone(
        val phone: String,
    ) : StaffIntent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : StaffIntent

    data class LoadStaff(
        val staff: Staff,
    ) : StaffIntent

    object Proceed : StaffIntent

    object ConsumeEffect : StaffIntent
}
