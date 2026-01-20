package eg.edu.cu.csds.icare.feature.home.screen.lab

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.util.UiText
import eg.edu.cu.csds.icare.feature.home.common.TopBar

@Stable
data class LabListState(
    val topBar: TopBar = TopBar.ServiceTopBar,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val labs: List<LabImagingCenter> = emptyList(),
    val effect: LabListEffect? = null,
)

sealed interface LabListEffect {
    object OnBackClick : LabListEffect

    data class ShowError(
        val message: UiText,
    ) : LabListEffect
}

sealed interface LabListIntent {
    object OnBackClick : LabListIntent

    object Refresh : LabListIntent

    data class ChangeTopBar(
        val topBar: TopBar,
    ) : LabListIntent

    data class UpdateSearchQuery(
        val query: String,
    ) : LabListIntent

    object Search : LabListIntent

    object ConsumeEffect : LabListIntent
}
