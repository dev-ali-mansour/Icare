package eg.edu.cu.csds.icare.feature.home.screen.lab

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface LabListEffect {
    object OnBackClick : LabListEffect

    data class ShowError(
        val message: UiText,
    ) : LabListEffect
}
