package eg.edu.cu.csds.icare.feature.home.screen.pharmacy

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PharmacyListEffect {
    object OnBackClick : PharmacyListEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacyListEffect
}
