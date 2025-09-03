package eg.edu.cu.csds.icare.admin.screen.clinician

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface ClinicianEffect {
    object ShowSuccess : ClinicianEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicianEffect
}
