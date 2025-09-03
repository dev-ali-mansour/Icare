package eg.edu.cu.csds.icare.consultation.screen

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy

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
