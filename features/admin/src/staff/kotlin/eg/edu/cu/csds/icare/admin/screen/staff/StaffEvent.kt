package eg.edu.cu.csds.icare.admin.screen.staff

import eg.edu.cu.csds.icare.core.domain.model.Staff

sealed interface StaffEvent {
    data class UpdateFirstName(
        val firstName: String,
    ) : StaffEvent

    data class UpdateLastName(
        val lastName: String,
    ) : StaffEvent

    data class UpdateCenterId(
        val centerId: Long,
    ) : StaffEvent

    data class UpdateCentersExpanded(
        val isExpanded: Boolean,
    ) : StaffEvent

    data class UpdateEmail(
        val email: String,
    ) : StaffEvent

    data class UpdatePhone(
        val phone: String,
    ) : StaffEvent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : StaffEvent

    data class LoadStaff(
        val staff: Staff,
    ) : StaffEvent

    object Proceed : StaffEvent

    object ConsumeEffect : StaffEvent
}
