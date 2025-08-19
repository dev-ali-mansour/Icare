package eg.edu.cu.csds.icare.admin.screen.pharmacy.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PharmacyListSingleEvent {
    data class NavigateToPharmacyDetails(
        val pharmacy: Pharmacy,
    ) : PharmacyListSingleEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacyListSingleEvent

    data class ShowError(
        val message: UiText,
    ) : PharmacyListSingleEvent
}
