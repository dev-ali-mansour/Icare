package eg.edu.cu.csds.icare.feature.admin.screen.clinician.list

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class ClinicianListState(
    val isLoading: Boolean = false,
    val clinicians: List<Clinician> = emptyList(),
    val effect: ClinicianListEffect? = null,
)

sealed interface ClinicianListEffect {
    data class NavigateToClinicianDetails(
        val clinician: Clinician,
    ) : ClinicianListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicianListEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicianListEffect
}

sealed interface ClinicianListIntent {
    object Refresh : ClinicianListIntent

    data class SelectClinician(
        val clinician: Clinician,
    ) : ClinicianListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicianListIntent

    object ConsumeEffect : ClinicianListIntent
}
