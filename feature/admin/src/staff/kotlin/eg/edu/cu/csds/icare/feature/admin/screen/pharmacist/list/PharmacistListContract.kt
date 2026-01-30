package eg.edu.cu.csds.icare.feature.admin.screen.pharmacist.list

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class PharmacistListState(
    val isLoading: Boolean = false,
    val pharmacists: List<Pharmacist> = emptyList(),
    val effect: PharmacistListEffect? = null,
)

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

sealed interface PharmacistListIntent {
    object Refresh : PharmacistListIntent

    data class SelectPharmacist(
        val pharmacist: Pharmacist,
    ) : PharmacistListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : PharmacistListIntent

    object ConsumeEffect : PharmacistListIntent
}
