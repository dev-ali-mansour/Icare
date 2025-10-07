package eg.edu.cu.csds.icare.feature.admin.screen.center.list

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.util.UiText

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
