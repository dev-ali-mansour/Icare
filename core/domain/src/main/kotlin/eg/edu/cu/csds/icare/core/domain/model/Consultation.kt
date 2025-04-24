package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Consultation(
    val token: String = "",
    val id: Long = 0,
    val doctorId: String = "",
    val doctorName: String = "",
    @SerialName("consultationTime")
    val dateTime: Long = 0,
    val patientImage: String = "",
    val patientName: String = "",
    val diagnosis: String = "",
    val pharmacyId: Long,
    val medication: String = "",
    val prescriptionStatusId: Short = 1,
    @Transient
    val prescriptionStatus: String = "",
    val labCenterId: Long,
    val labTest: String = "",
    val labTestStatusId: Short = 1,
    @Transient
    val labTestStatus: String = "",
    val imagingCenterId: Long,
    val imagingTest: String = "",
    val imgTestStatusId: Short = 1,
    @Transient
    val imgTestStatus: String = "",
    val followUpdDate: Long = 0,
)
