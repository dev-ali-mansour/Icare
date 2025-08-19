package eg.edu.cu.csds.icare.admin.screen.pharmacy

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

sealed interface PharmacyIntent {
    data class UpdateName(
        val name: String,
    ) : PharmacyIntent

    data class UpdatePhone(
        val phone: String,
    ) : PharmacyIntent

    data class UpdateAddress(
        val address: String,
    ) : PharmacyIntent

    data class SelectPharmacy(
        val pharmacy: Pharmacy,
    ) : PharmacyIntent

    object Proceed : PharmacyIntent
}
