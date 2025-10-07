package eg.edu.cu.csds.icare.feature.admin.screen.clinician

import eg.edu.cu.csds.icare.core.domain.model.Clinician

sealed interface ClinicianEvent {
    data class UpdateFirstName(
        val firstName: String,
    ) : ClinicianEvent

    data class UpdateLastName(
        val lastName: String,
    ) : ClinicianEvent

    data class UpdateClinicId(
        val clinicId: Long,
    ) : ClinicianEvent

    data class UpdateClinicsExpanded(
        val isExpanded: Boolean,
    ) : ClinicianEvent

    data class UpdateEmail(
        val email: String,
    ) : ClinicianEvent

    data class UpdatePhone(
        val phone: String,
    ) : ClinicianEvent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : ClinicianEvent

    data class LoadClinician(
        val clinician: Clinician,
    ) : ClinicianEvent

    object Proceed : ClinicianEvent

    object ConsumeEffect : ClinicianEvent
}
