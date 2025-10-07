package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PharmacyEffect {
    object ShowSuccess : PharmacyEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacyEffect
}
