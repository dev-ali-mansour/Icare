package eg.edu.cu.csds.icare.feature.admin.screen.center.list

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class CenterListState(
    val isLoading: Boolean = false,
    val centers: List<LabImagingCenter> = emptyList(),
    val effect: CenterListEffect? = null,
)

sealed interface CenterListEffect {
    data class NavigateToCenterDetails(
        val center: LabImagingCenter,
    ) : CenterListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : CenterListEffect

    data class ShowError(
        val message: UiText,
    ) : CenterListEffect
}

sealed interface CenterListIntent {
    object Refresh : CenterListIntent

    data class SelectCenter(
        val center: LabImagingCenter,
    ) : CenterListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : CenterListIntent

    object ConsumeEffect : CenterListIntent
}
