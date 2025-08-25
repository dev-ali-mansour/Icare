package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PharmacyListEffect {
    data class NavigateToPharmacyDetails(
        val pharmacy: Pharmacy,
    ) : PharmacyListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacyListEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacyListEffect
}
