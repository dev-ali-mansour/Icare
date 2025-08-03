package eg.edu.cu.csds.icare.auth.screen.signup

sealed interface SignUpAction {
    data class UpdateFirstName(
        val firstName: String,
    ) : SignUpAction

    data class UpdateLastName(
        val lastName: String,
    ) : SignUpAction

    data class UpdateEmail(
        val email: String,
    ) : SignUpAction

    data class UpdateBirthDate(
        val birthDate: String,
    ) : SignUpAction

    data class UpdateGender(
        val gender: Short,
    ) : SignUpAction

    data class UpdateIsGenderExpanded(
        val isGenderExpanded: Boolean,
    ) : SignUpAction

    data class UpdateNationalId(
        val nationalId: String,
    ) : SignUpAction

    data class UpdatePhone(
        val phone: String,
    ) : SignUpAction

    data class UpdateAddress(
        val address: String,
    ) : SignUpAction

    data class UpdateWeight(
        val weight: Double,
    ) : SignUpAction

    data class UpdateChronicDiseases(
        val chronicDiseases: String,
    ) : SignUpAction

    data class UpdateCurrentMedications(
        val currentMedications: String,
    ) : SignUpAction

    data class UpdateAllergies(
        val allergies: String,
    ) : SignUpAction

    data class UpdatePastSurgeries(
        val pastSurgeries: String,
    ) : SignUpAction

    data class UpdatePassword(
        val password: String,
    ) : SignUpAction

    object TogglePasswordVisibility : SignUpAction

    object NavigateToSignInScreen : SignUpAction
}
