package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

sealed interface PharmacyListIntent {
    object Refresh : PharmacyListIntent

    data class SelectPharmacy(
        val pharmacy: Pharmacy,
    ) : PharmacyListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacyListIntent
}
