package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy.list

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class PharmacyListState(
    val isLoading: Boolean = false,
    val pharmacies: List<Pharmacy> = emptyList(),
    val effect: PharmacyListEffect? = null,
)

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

sealed interface PharmacyListIntent {
    object Refresh : PharmacyListIntent

    data class SelectPharmacy(
        val pharmacy: Pharmacy,
    ) : PharmacyListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacyListIntent

    object ConsumeEffect : PharmacyListIntent
}
