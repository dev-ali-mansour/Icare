package eg.edu.cu.csds.icare.admin.screen.pharmacist

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface PharmacistEffect {
    object ShowSuccess : PharmacistEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacistEffect
}
