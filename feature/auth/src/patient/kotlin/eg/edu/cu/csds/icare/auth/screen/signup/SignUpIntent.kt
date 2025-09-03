package eg.edu.cu.csds.icare.auth.screen.signup

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
}
