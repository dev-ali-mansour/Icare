package eg.edu.cu.csds.icare.admin.screen.doctor.list

import eg.edu.cu.csds.icare.core.domain.model.Doctor

sealed interface DoctorListIntent {
    object Refresh : DoctorListIntent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : DoctorListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : DoctorListIntent
}
