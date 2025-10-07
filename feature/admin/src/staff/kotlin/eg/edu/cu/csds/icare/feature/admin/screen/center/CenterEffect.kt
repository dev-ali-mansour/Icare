package eg.edu.cu.csds.icare.feature.admin.screen.center

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface CenterEffect {
    object ShowSuccess : CenterEffect

    data class ShowError(
        val message: UiText,
    ) : CenterEffect
}
