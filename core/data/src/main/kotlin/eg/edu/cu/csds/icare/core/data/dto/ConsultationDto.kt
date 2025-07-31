package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsultationDto(
    val token: String = "",
    val id: Long = 0,
    val appointment: AppointmentDto = AppointmentDto(),
    @SerialName("date") val dateTime: Long = 0,
    val diagnosis: String = "",
    val pharmacyId: Long = 1,
    val medications: String = "",
    @SerialName("prescriptionsStatus") val prescriptionStatusId: Short = 1,
    val labCenterId: Long = 1,
    val labTests: String = "",
    val labTestStatusId: Short = 1,
    val imagingCenterId: Long = 1,
    val imagingTests: String = "",
    @SerialName("imagingCenterStatus") val imgTestStatusId: Short = 1,
    val followUpdDate: Long = 0,
)
