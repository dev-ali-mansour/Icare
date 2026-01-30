package eg.edu.cu.csds.icare.feature.admin.screen.doctor.list

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class DoctorListState(
    val isLoading: Boolean = false,
    val doctors: List<Doctor> = emptyList(),
    val effect: DoctorListEffect? = null,
)

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

sealed interface DoctorListIntent {
    object Refresh : DoctorListIntent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : DoctorListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : DoctorListIntent

    object ConsumeEffect : DoctorListIntent
}
