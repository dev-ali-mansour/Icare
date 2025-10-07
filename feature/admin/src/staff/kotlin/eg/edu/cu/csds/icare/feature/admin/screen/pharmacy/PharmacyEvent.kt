package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

sealed interface PharmacyEvent {
    data class UpdateName(
        val name: String,
    ) : PharmacyEvent

    data class UpdatePhone(
        val phone: String,
    ) : PharmacyEvent

    data class UpdateAddress(
        val address: String,
    ) : PharmacyEvent

    data class LoadPharmacy(
        val pharmacy: Pharmacy,
    ) : PharmacyEvent

    object Proceed : PharmacyEvent

    object ConsumeEffect : PharmacyEvent
}
