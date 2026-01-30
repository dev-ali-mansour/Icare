package eg.edu.cu.csds.icare.feature.home.screen.imaging

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.view.TopSearchBarState

@Stable
data class ImagingCenterListState(
    val searchBarState: TopSearchBarState = TopSearchBarState.Collapsed,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val centers: List<LabImagingCenter> = emptyList(),
    val effect: ImagingCenterListEffect? = null,
)

sealed interface ImagingCenterListEffect {
    object OnBackClick : ImagingCenterListEffect

    data class ShowError(
        val message: UiText,
    ) : ImagingCenterListEffect
}

sealed interface ImagingCenterListIntent {
    object OnBackClick : ImagingCenterListIntent

    object Refresh : ImagingCenterListIntent

    data class UpdateSearchTopBarState(
        val state: TopSearchBarState,
    ) : ImagingCenterListIntent

    data class UpdateSearchQuery(
        val query: String,
    ) : ImagingCenterListIntent

    object Search : ImagingCenterListIntent

    object ConsumeEffect : ImagingCenterListIntent
}
