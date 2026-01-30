package eg.edu.cu.csds.icare.feature.appointment.screen.doctors

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.core.ui.view.TopSearchBarState

@Stable
data class DoctorListState(
    val searchBarState: TopSearchBarState = TopSearchBarState.Collapsed,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val doctors: List<Doctor> = emptyList(),
    val effect: DoctorListEffect? = null,
)

sealed interface DoctorListEffect {
    object OnBackClick : DoctorListEffect

    data class NavigateToBookingRoute(
        val doctor: Doctor,
    ) : DoctorListEffect

    data class ShowError(
        val message: UiText,
    ) : DoctorListEffect
}

sealed interface DoctorListIntent {
    object OnBackClick : DoctorListIntent

    object Refresh : DoctorListIntent

    data class UpdateSearchTopBarState(
        val state: TopSearchBarState,
    ) : DoctorListIntent

    data class UpdateSearchQuery(
        val query: String,
    ) : DoctorListIntent

    object Search : DoctorListIntent

    data class SelectDoctor(
        val doctor: Doctor,
    ) : DoctorListIntent

    object ConsumeEffect : DoctorListIntent
}
