package eg.edu.cu.csds.icare.auth.screen.signup

import eg.edu.cu.csds.icare.core.ui.util.UiText

data class SignUpUIState(
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean = false,
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var birthDate: Long = System.currentTimeMillis(),
    var gender: Short = 0,
    var isGenderExpanded: Boolean = false,
    var nationalId: String = "",
    var phone: String = "",
    var address: String = "",
    var weight: Double = 0.0,
    var chronicDiseases: String = "",
    var currentMedications: String = "",
    var allergies: String = "",
    var pastSurgeries: String = "",
    var password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: UiText? = null,
)
