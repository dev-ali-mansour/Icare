package eg.edu.cu.csds.icare.feature.admin.screen.clinic

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

    data class LoadClinic(
        val clinic: Clinic,
    ) : ClinicEvent

    object Proceed : ClinicEvent

    object ConsumeEffect : ClinicEvent
}
