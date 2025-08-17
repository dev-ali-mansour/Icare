package eg.edu.cu.csds.icare.admin.screen.clinic.doctor.list

import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.util.UiText

sealed interface DoctorListSingleEvent {
    data class NavigateToDoctorDetails(
        val doctor: Doctor,
    ) : DoctorListSingleEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : DoctorListSingleEvent

    data class ShowError(
        val message: UiText,
    ) : DoctorListSingleEvent
}
