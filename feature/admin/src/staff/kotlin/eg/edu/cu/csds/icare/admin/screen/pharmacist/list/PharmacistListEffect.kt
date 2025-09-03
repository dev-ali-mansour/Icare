package eg.edu.cu.csds.icare.admin.screen.pharmacist.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PharmacistListEffect {
    data class NavigateToPharmacistDetails(
        val pharmacist: Pharmacist,
    ) : PharmacistListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacistListEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacistListEffect
}
