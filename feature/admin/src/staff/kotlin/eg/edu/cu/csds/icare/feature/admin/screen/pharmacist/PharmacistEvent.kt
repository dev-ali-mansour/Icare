package eg.edu.cu.csds.icare.feature.admin.screen.pharmacist

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist

sealed interface PharmacistEvent {
    data class UpdateFirstName(
        val firstName: String,
    ) : PharmacistEvent

    data class UpdateLastName(
        val lastName: String,
    ) : PharmacistEvent

    data class UpdatePharmacyId(
        val pharmacyId: Long,
    ) : PharmacistEvent

    data class UpdatePharmaciesExpanded(
        val isExpanded: Boolean,
    ) : PharmacistEvent

    data class UpdateEmail(
        val email: String,
    ) : PharmacistEvent

    data class UpdatePhone(
        val phone: String,
    ) : PharmacistEvent

    data class UpdateProfilePicture(
        val profilePicture: String,
    ) : PharmacistEvent

    data class LoadPharmacist(
        val pharmacist: Pharmacist,
    ) : PharmacistEvent

    object Proceed : PharmacistEvent

    object ConsumeEffect : PharmacistEvent
}
