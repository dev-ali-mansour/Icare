package eg.edu.cu.csds.icare.admin.screen.staff

import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface StaffEffect {
    object ShowSuccess : StaffEffect

    data class ShowError(
        val message: UiText,
    ) : StaffEffect
}
