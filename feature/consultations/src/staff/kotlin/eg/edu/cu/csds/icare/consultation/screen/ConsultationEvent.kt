package eg.edu.cu.csds.icare.consultation.screen

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Consultation

sealed interface ConsultationEvent {
    data class NavigateToMedicalRecord(
        val patientId: String,
    ) : ConsultationEvent

    object Refresh : ConsultationEvent

    data class UpdateAppointment(
        val appointment: Appointment,
    ) : ConsultationEvent

    data class UpdateDiagnosis(
        val diagnosis: String,
    ) : ConsultationEvent

    data class UpdatePharmacyId(
        val pharmacyId: Long,
    ) : ConsultationEvent

    data class UpdatePharmaciesExpanded(
        val isExpanded: Boolean,
    ) : ConsultationEvent

    data class UpdateMedications(
        val medications: String,
    ) : ConsultationEvent

    data class UpdateLabCenterId(
        val labCenterId: Long,
    ) : ConsultationEvent

    data class UpdateLabCentersExpanded(
        val isExpanded: Boolean,
    ) : ConsultationEvent

    data class UpdateLabTests(
        val labTests: String,
    ) : ConsultationEvent

    data class UpdateImagingCenterId(
        val imagingCenterId: Long,
    ) : ConsultationEvent

    data class UpdateImagingCentersExpanded(
        val isExpanded: Boolean,
    ) : ConsultationEvent

    data class UpdateImagingTests(
        val imagingTests: String,
    ) : ConsultationEvent

    data class UpdateFollowUpdDate(
        val followUpdDate: Long,
    ) : ConsultationEvent

    data class LoadConsultation(
        val consultation: Consultation,
    ) : ConsultationEvent

    object Proceed : ConsultationEvent

    object ConsumeEffect : ConsultationEvent
}
