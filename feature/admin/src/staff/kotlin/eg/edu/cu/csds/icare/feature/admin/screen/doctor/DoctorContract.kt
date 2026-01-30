package eg.edu.cu.csds.icare.feature.admin.screen.doctor

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class DoctorState(
    val isLoading: Boolean = false,
    val clinics: List<Clinic> = emptyList(),
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val clinicId: Long = 0,
    val isClinicsExpanded: Boolean = false,
    val email: String = "",
    val phone: String = "",
    val speciality: String = "",
    val fromTime: Long = System.currentTimeMillis(),
    val toTime: Long = fromTime.plus(other = 5 * 60 * 60 * 1000),
    val rating: Double = 5.0,
    val ratingReadOnly: Boolean = true,
    val price: Double = 0.0,
    val profilePicture: String = "",
    val effect: DoctorEffect? = null,
)

sealed interface DoctorEffect {
    object ShowSuccess : DoctorEffect

    data class ShowError(
        val message: UiText,
    ) : DoctorEffect
}

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

    data class LoadDoctor(
        val doctor: Doctor,
    ) : DoctorIntent

    object Proceed : DoctorIntent

    object ConsumeEffect : DoctorIntent
}
