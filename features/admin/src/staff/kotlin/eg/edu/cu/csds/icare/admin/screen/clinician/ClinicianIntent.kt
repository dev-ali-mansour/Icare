package eg.edu.cu.csds.icare.admin.screen.clinician

import eg.edu.cu.csds.icare.core.domain.model.Clinician

sealed interface ClinicianIntent {
    data class UpdateFirstName(
        val firstName: String,
    ) : ClinicianIntent

    data class UpdateLastName(
        val lastName: String,
    ) : ClinicianIntent

    data class UpdateClinicId(
        val clinicId: Long,
    ) : ClinicianIntent

    data class UpdateClinicsExpanded(
        val isExpanded: Boolean,
    ) : ClinicianIntent

    data class UpdateEmail(
        val email: String,
    ) : ClinicianIntent

    data class UpdatePhone(
        val phone: String,
    ) : ClinicianIntent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : ClinicianIntent

    data class SelectClinician(
        val clinician: Clinician,
    ) : ClinicianIntent

    object Proceed : ClinicianIntent
}
