package eg.edu.cu.csds.icare.admin.screen.clinic

import eg.edu.cu.csds.icare.core.domain.model.Clinic

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

    data class SelectClinic(
        val clinic: Clinic,
    ) : ClinicIntent

    object Proceed : ClinicIntent
}
