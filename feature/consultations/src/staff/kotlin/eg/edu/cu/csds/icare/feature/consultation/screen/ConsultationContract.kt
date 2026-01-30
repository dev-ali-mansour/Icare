package eg.edu.cu.csds.icare.feature.consultation.screen

import androidx.compose.runtime.Stable
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.ui.util.UiText

@Stable
data class ConsultationState(
    val isLoading: Boolean = false,
    val pharmacies: List<Pharmacy> = emptyList(),
    val centers: List<LabImagingCenter> = emptyList(),
    val id: Long = 0,
    val appointment: Appointment = Appointment(),
    val dateTime: Long = 0,
    val diagnosis: String = "",
    val pharmacyId: Long = 1,
    val isPharmaciesExpanded: Boolean = false,
    val medications: String = "",
    val prescriptionStatusId: Short = 1,
    val prescriptionStatus: String = "",
    val labCenterId: Long = 1,
    val isLabCentersExpanded: Boolean = false,
    val labTests: String = "",
    val labTestStatusId: Short = 1,
    val labTestStatus: String = "",
    val imagingCenterId: Long = 1,
    val isImagingCentersExpanded: Boolean = false,
    val imagingTests: String = "",
    val imgTestStatusId: Short = 1,
    val imgTestStatus: String = "",
    val followUpdDate: Long = 0,
    val readOnly: Boolean = true,
    val effect: ConsultationEffect? = null,
)

sealed interface ConsultationEffect {
    object ShowSuccess : ConsultationEffect

    data class NavigateToMedicalRecord(
        val patientId: String,
    ) : ConsultationEffect

    data class ShowError(
        val message: UiText,
    ) : ConsultationEffect
}

sealed interface ConsultationIntent {
    data class NavigateToMedicalRecord(
        val patientId: String,
    ) : ConsultationIntent

    object Refresh : ConsultationIntent

    data class UpdateAppointment(
        val appointment: Appointment,
    ) : ConsultationIntent

    data class UpdateDiagnosis(
        val diagnosis: String,
    ) : ConsultationIntent

    data class UpdatePharmacyId(
        val pharmacyId: Long,
    ) : ConsultationIntent

    data class UpdatePharmaciesExpanded(
        val isExpanded: Boolean,
    ) : ConsultationIntent

    data class UpdateMedications(
        val medications: String,
    ) : ConsultationIntent

    data class UpdateLabCenterId(
        val labCenterId: Long,
    ) : ConsultationIntent

    data class UpdateLabCentersExpanded(
        val isExpanded: Boolean,
    ) : ConsultationIntent

    data class UpdateLabTests(
        val labTests: String,
    ) : ConsultationIntent

    data class UpdateImagingCenterId(
        val imagingCenterId: Long,
    ) : ConsultationIntent

    data class UpdateImagingCentersExpanded(
        val isExpanded: Boolean,
    ) : ConsultationIntent

    data class UpdateImagingTests(
        val imagingTests: String,
    ) : ConsultationIntent

    data class UpdateFollowUpdDate(
        val followUpdDate: Long,
    ) : ConsultationIntent

    data class LoadConsultation(
        val consultation: Consultation,
    ) : ConsultationIntent

    object Proceed : ConsultationIntent

    object ConsumeEffect : ConsultationIntent
}
