package eg.edu.cu.csds.icare.feature.admin.screen.clinic

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class ClinicState(
    val isLoading: Boolean = false,
    val id: Long = 0,
    val name: String = "",
    val type: String = "",
    val phone: String = "",
    val address: String = "",
    val isOpen: Boolean = true,
    val effect: ClinicEffect? = null,
)

sealed interface ClinicEffect {
    object ShowSuccess : ClinicEffect

    data class ShowError(
        val message: UiText,
    ) : ClinicEffect
}

sealed interface ClinicIntent {
    data class UpdateName(
        val name: String,
    ) : ClinicIntent

    data class UpdateType(
        val type: String,
    ) : ClinicIntent

    data class UpdatePhone(
        val phone: String,
    ) : ClinicIntent

    data class UpdateAddress(
        val address: String,
    ) : ClinicIntent

    data class UpdateIsOpen(
        val isOpen: Boolean,
    ) : ClinicIntent

    data class LoadClinic(
        val clinic: Clinic,
    ) : ClinicIntent

    object Proceed : ClinicIntent

    object ConsumeEffect : ClinicIntent
}
