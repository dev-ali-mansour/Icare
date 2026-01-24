package eg.edu.cu.csds.icare.feature.admin.screen.clinic.list

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class ClinicListState(
    val isLoading: Boolean = false,
    val clinics: List<Clinic> = emptyList(),
    val effect: ClinicListEffect? = null,
)

sealed interface ClinicListEffect {
    data class NavigateToClinicDetails(
        val clinic: Clinic,
    ) : ClinicListEffect

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicListEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicListEffect
}

sealed interface ClinicListIntent {
    object Refresh : ClinicListIntent

    data class SelectClinic(
        val clinic: Clinic,
    ) : ClinicListIntent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : ClinicListIntent

    object ConsumeEffect : ClinicListIntent
}
