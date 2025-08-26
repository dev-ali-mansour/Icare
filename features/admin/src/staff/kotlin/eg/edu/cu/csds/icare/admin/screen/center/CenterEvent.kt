package eg.edu.cu.csds.icare.admin.screen.center

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter

sealed interface CenterEvent {
    data class UpdateName(
        val name: String,
    ) : CenterEvent

    data class UpdateType(
        val type: Short,
    ) : CenterEvent

    data class UpdateTypesExpanded(
        val expanded: Boolean,
    ) : CenterEvent

    data class UpdatePhone(
        val phone: String,
    ) : CenterEvent

    data class UpdateAddress(
        val address: String,
    ) : CenterEvent

    data class SelectCenter(
        val center: LabImagingCenter,
    ) : CenterEvent

    object Proceed : CenterEvent

    object ConsumeEffect : CenterEvent
}
