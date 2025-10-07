package eg.edu.cu.csds.icare.feature.admin.screen.doctor

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface DoctorEffect {
    object ShowSuccess : DoctorEffect

    data class ShowError(
        val message: UiText,
    ) : DoctorEffect
}
