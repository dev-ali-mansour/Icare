package eg.edu.cu.csds.icare.auth.screen.signup

import eg.edu.cu.csds.icare.core.ui.util.UiText

data class SignUpUIState(
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean = false,
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
    val alertMessage: UiText? = null,
)
