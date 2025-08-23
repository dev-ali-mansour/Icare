package eg.edu.cu.csds.icare.admin.screen.pharmacist

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist

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

    data class SelectPharmacist(
        val pharmacist: Pharmacist,
    ) : PharmacistIntent

    object Proceed : PharmacistIntent

    object ConsumeEffect : PharmacistIntent
}
