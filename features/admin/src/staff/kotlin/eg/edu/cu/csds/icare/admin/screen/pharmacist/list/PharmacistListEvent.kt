package eg.edu.cu.csds.icare.admin.screen.pharmacist.list

import eg.edu.cu.csds.icare.core.domain.model.Pharmacist

sealed interface PharmacistListEvent {
    object Refresh : PharmacistListEvent

    data class SelectPharmacist(
        val pharmacist: Pharmacist,
    ) : PharmacistListEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacistListEvent

    object ConsumeEffect : PharmacistListEvent
}
