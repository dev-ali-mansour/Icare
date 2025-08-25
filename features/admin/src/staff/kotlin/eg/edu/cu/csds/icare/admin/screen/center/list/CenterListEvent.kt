package eg.edu.cu.csds.icare.admin.screen.center.list

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

sealed interface CenterListEvent {
    object Refresh : CenterListEvent

    data class SelectCenter(
        val center: LabImagingCenter,
    ) : CenterListEvent

    data class UpdateFabExpanded(
        val isExpanded: Boolean,
    ) : CenterListEvent

    object ConsumeEffect : CenterListEvent
}
