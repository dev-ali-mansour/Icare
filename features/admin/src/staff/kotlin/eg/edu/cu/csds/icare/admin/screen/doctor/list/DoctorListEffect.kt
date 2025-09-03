package eg.edu.cu.csds.icare.admin.screen.doctor.list

import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface DoctorListEffect {
    data class NavigateToDoctorDetails(
        val doctor: Doctor,
    ) : DoctorListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : DoctorListEffect

    data class ShowError(
        val message: UiText,
    ) : DoctorListEffect
}
