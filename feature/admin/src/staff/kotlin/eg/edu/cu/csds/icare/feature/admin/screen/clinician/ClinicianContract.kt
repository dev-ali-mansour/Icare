package eg.edu.cu.csds.icare.feature.admin.screen.clinician

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class ClinicianState(
    val isLoading: Boolean = false,
    val clinics: List<Clinic> = emptyList(),
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val clinicId: Long = 0,
    val isClinicsExpanded: Boolean = false,
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
    val effect: ClinicianEffect? = null,
)

sealed interface ClinicianEffect {
    object ShowSuccess : ClinicianEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicianEffect
}

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

    data class LoadClinician(
        val clinician: Clinician,
    ) : ClinicianIntent

    object Proceed : ClinicianIntent

    object ConsumeEffect : ClinicianIntent
}
