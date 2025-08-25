package eg.edu.cu.csds.icare.admin.screen.clinic

import eg.edu.cu.csds.icare.core.domain.model.Clinic

sealed interface ClinicEvent {
    data class UpdateName(
        val name: String,
    ) : ClinicEvent

    data class UpdateType(
        val type: String,
    ) : ClinicEvent

    data class UpdatePhone(
        val phone: String,
    ) : ClinicEvent

    data class UpdateAddress(
        val address: String,
    ) : ClinicEvent

    data class UpdateIsOpen(
        val isOpen: Boolean,
    ) : ClinicEvent

    data class SelectClinic(
        val clinic: Clinic,
    ) : ClinicEvent

    object Proceed : ClinicEvent

    object ConsumeEffect : ClinicEvent
}
