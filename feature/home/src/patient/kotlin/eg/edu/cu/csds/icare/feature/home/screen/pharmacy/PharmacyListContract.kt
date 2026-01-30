package eg.edu.cu.csds.icare.feature.home.screen.pharmacy

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.view.TopSearchBarState

@Stable
data class PharmacyListState(
    val searchBarState: TopSearchBarState = TopSearchBarState.Collapsed,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val pharmacies: List<Pharmacy> = emptyList(),
    val effect: PharmacyListEffect? = null,
)

sealed interface PharmacyListEffect {
    object OnBackClick : PharmacyListEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacyListEffect
}

sealed interface PharmacyListIntent {
    object OnBackClick : PharmacyListIntent

    object Refresh : PharmacyListIntent

    data class UpdateSearchTopBarState(
        val state: TopSearchBarState,
    ) : PharmacyListIntent

    data class UpdateSearchQuery(
        val query: String,
    ) : PharmacyListIntent

    object Search : PharmacyListIntent

    object ConsumeEffect : PharmacyListIntent
}
