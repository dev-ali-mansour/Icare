package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

sealed interface PharmacyListEvent {
    object Refresh : PharmacyListEvent

    data class SelectPharmacy(
        val pharmacy: Pharmacy,
    ) : PharmacyListEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacyListEvent

    object ConsumeEffect : PharmacyListEvent
}
