package eg.edu.cu.csds.icare.home.screen.lab

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface LabListEffect {
    object OnBackClick : LabListEffect

    data class ShowError(
        val message: UiText,
    ) : LabListEffect
}
