package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class PharmacyState(
    val isLoading: Boolean = false,
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val effect: PharmacyEffect? = null,
)

sealed interface PharmacyEffect {
    object ShowSuccess : PharmacyEffect

    data class ShowError(
        val message: UiText,
    ) : PharmacyEffect
}

sealed interface PharmacyIntent {
    data class UpdateName(
        val name: String,
    ) : PharmacyIntent

    data class UpdatePhone(
        val phone: String,
    ) : PharmacyIntent

    data class UpdateAddress(
        val address: String,
    ) : PharmacyIntent

    data class LoadPharmacy(
        val pharmacy: Pharmacy,
    ) : PharmacyIntent

    object Proceed : PharmacyIntent

    object ConsumeEffect : PharmacyIntent
}
