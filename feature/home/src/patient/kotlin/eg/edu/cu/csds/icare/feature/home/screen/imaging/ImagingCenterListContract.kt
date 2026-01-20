package eg.edu.cu.csds.icare.feature.home.screen.imaging

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.feature.home.common.TopBar

@Stable
data class ImagingCenterListState(
    val topBar: TopBar = TopBar.ServiceTopBar,
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

    data class ChangeTopBar(
        val topBar: TopBar,
    ) : ImagingCenterListIntent

    data class UpdateSearchQuery(
        val query: String,
    ) : ImagingCenterListIntent

    object Search : ImagingCenterListIntent

    object ConsumeEffect : ImagingCenterListIntent
}
