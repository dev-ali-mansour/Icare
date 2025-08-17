package eg.edu.cu.csds.icare.admin.screen.doctor

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface DoctorIntent {
    data class UpdateFirstName(
        val firstName: String,
    ) : DoctorIntent

    data class UpdateLastName(
        val lastName: String,
    ) : DoctorIntent

    data class UpdateClinicId(
        val clinicId: Long,
    ) : DoctorIntent

    data class UpdateClinicsExpanded(
        val isExpanded: Boolean,
    ) : DoctorIntent

    data class UpdateEmail(
        val email: String,
    ) : DoctorIntent

    data class UpdatePhone(
        val phone: String,
    ) : DoctorIntent

    data class UpdateSpeciality(
        val speciality: String,
    ) : DoctorIntent

    data class UpdateFromTime(
        val fromTime: Long,
    ) : DoctorIntent

    data class UpdateToTime(
        val toTime: Long,
    ) : DoctorIntent

    data class UpdatePrice(
        val price: Double,
    ) : DoctorIntent

    data class UpdateRating(
        val rating: Double,
    ) : DoctorIntent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : DoctorIntent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : DoctorIntent

    object Proceed : DoctorIntent
}
