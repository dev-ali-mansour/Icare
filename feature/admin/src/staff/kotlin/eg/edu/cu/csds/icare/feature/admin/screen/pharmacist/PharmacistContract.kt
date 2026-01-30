package eg.edu.cu.csds.icare.feature.admin.screen.pharmacist

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class PharmacistState(
    val isLoading: Boolean = false,
    val pharmacies: List<Pharmacy> = emptyList(),
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "$firstName $lastName",
    val pharmacyId: Long = 0,
    val isPharmaciesExpanded: Boolean = false,
    val pharmacyName: String = "",
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
    val effect: PharmacistEffect? = null,
)

sealed interface PharmacistEffect {
    object ShowSuccess : PharmacistEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacistEffect
}

sealed interface PharmacistIntent {
    data class UpdateFirstName(
        val firstName: String,
    ) : PharmacistIntent

    data class UpdateLastName(
        val lastName: String,
    ) : PharmacistIntent

    data class UpdatePharmacyId(
        val pharmacyId: Long,
    ) : PharmacistIntent

    data class UpdatePharmaciesExpanded(
        val isExpanded: Boolean,
    ) : PharmacistIntent

    data class UpdateEmail(
        val email: String,
    ) : PharmacistIntent

    data class UpdatePhone(
        val phone: String,
    ) : PharmacistIntent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : PharmacistIntent

    data class LoadPharmacist(
        val pharmacist: Pharmacist,
    ) : PharmacistIntent

    object Proceed : PharmacistIntent

    object ConsumeEffect : PharmacistIntent
}
