package eg.edu.cu.csds.icare.feature.home.screen.imaging

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ImagingCenterListEffect {
    object OnBackClick : ImagingCenterListEffect

    data class ShowError(
        val message: UiText,
    ) : ImagingCenterListEffect
}
