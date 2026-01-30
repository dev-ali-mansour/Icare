package eg.edu.cu.csds.icare.feature.admin.screen.center

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class CenterState(
    val isLoading: Boolean = false,
    val id: Long = 0,
    val name: String = "",
    val type: Short = 0,
    val isTypesExpanded: Boolean = false,
    val phone: String = "",
    val address: String = "",
    val effect: CenterEffect? = null,
)

sealed interface CenterEffect {
    object ShowSuccess : CenterEffect

    data class ShowError(
        val message: UiText,
    ) : CenterEffect
}

sealed interface CenterIntent {
    data class UpdateName(
        val name: String,
    ) : CenterIntent

    data class UpdateType(
        val type: Short,
    ) : CenterIntent

    data class UpdateTypesExpanded(
        val expanded: Boolean,
    ) : CenterIntent

    data class UpdatePhone(
        val phone: String,
    ) : CenterIntent

    data class UpdateAddress(
        val address: String,
    ) : CenterIntent

    data class LoadCenter(
        val center: LabImagingCenter,
    ) : CenterIntent

    object Proceed : CenterIntent

    object ConsumeEffect : CenterIntent
}
