package eg.edu.cu.csds.icare.feature.admin.screen.doctor

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface DoctorEvent {
    data class UpdateFirstName(
        val firstName: String,
    ) : DoctorEvent

    data class UpdateLastName(
        val lastName: String,
    ) : DoctorEvent

    data class UpdateClinicId(
        val clinicId: Long,
    ) : DoctorEvent

    data class UpdateClinicsExpanded(
        val isExpanded: Boolean,
    ) : DoctorEvent

    data class UpdateEmail(
        val email: String,
    ) : DoctorEvent

    data class UpdatePhone(
        val phone: String,
    ) : DoctorEvent

    data class UpdateSpeciality(
        val speciality: String,
    ) : DoctorEvent

    data class UpdateFromTime(
        val fromTime: Long,
    ) : DoctorEvent

    data class UpdateToTime(
        val toTime: Long,
    ) : DoctorEvent

    data class UpdatePrice(
        val price: Double,
    ) : DoctorEvent

    data class UpdateRating(
        val rating: Double,
    ) : DoctorEvent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : DoctorEvent

    data class LoadDoctor(
        val doctor: Doctor,
    ) : DoctorEvent

    object Proceed : DoctorEvent

    object ConsumeEffect : DoctorEvent
}
