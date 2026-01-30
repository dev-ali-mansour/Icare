package eg.edu.cu.csds.icare.feature.auth.screen.signup

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class SignUpState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthDate: Long = System.currentTimeMillis(),
    val gender: Short = 0,
    val isGenderExpanded: Boolean = false,
    val nationalId: String = "",
    val phone: String = "",
    val address: String = "",
    val weight: Double = 0.0,
    val chronicDiseases: String = "",
    val currentMedications: String = "",
    val allergies: String = "",
    val pastSurgeries: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val showAlert: Boolean = false,
    val effect: SignUpEffect? = null,
)

sealed interface SignUpEffect {
    object SignUpSuccess : SignUpEffect

    data class ShowInfo(
        val message: UiText,
    ) : SignUpEffect

    data class ShowError(
        val message: UiText,
    ) : SignUpEffect
}

sealed interface SignUpIntent {
    data class UpdateFirstName(
        val firstName: String,
    ) : SignUpIntent

    data class UpdateLastName(
        val lastName: String,
    ) : SignUpIntent

    data class UpdateEmail(
        val email: String,
    ) : SignUpIntent

    data class UpdateBirthDate(
        val birthDate: Long,
    ) : SignUpIntent

    data class UpdateGender(
        val gender: Short,
    ) : SignUpIntent

    data class UpdateGenderExpanded(
        val isExpanded: Boolean,
    ) : SignUpIntent

    data class UpdateNationalId(
        val nationalId: String,
    ) : SignUpIntent

    data class UpdatePhone(
        val phone: String,
    ) : SignUpIntent

    data class UpdateAddress(
        val address: String,
    ) : SignUpIntent

    data class UpdateWeight(
        val weight: Double,
    ) : SignUpIntent

    data class UpdateChronicDiseases(
        val chronicDiseases: String,
    ) : SignUpIntent

    data class UpdateCurrentMedications(
        val currentMedications: String,
    ) : SignUpIntent

    data class UpdateAllergies(
        val allergies: String,
    ) : SignUpIntent

    data class UpdatePastSurgeries(
        val pastSurgeries: String,
    ) : SignUpIntent

    data class UpdatePassword(
        val password: String,
    ) : SignUpIntent

    object ToggleShowAlert : SignUpIntent

    object TogglePasswordVisibility : SignUpIntent

    object SubmitSignUp : SignUpIntent

    object NavigateToSignInScreen : SignUpIntent

    object ConsumeEffect : SignUpIntent
}
