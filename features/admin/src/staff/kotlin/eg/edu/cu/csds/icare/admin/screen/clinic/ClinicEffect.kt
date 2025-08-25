package eg.edu.cu.csds.icare.admin.screen.clinic

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicEffect {
    object ShowSuccess : ClinicEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicEffect
}
