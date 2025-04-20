package eg.edu.cu.csds.icare.core.domain.model

import eg.edu.cu.csds.icare.core.domain.util.divide
import eg.edu.cu.csds.icare.core.domain.util.getFormattedTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Doctor(
//    @Transient
    val token: String = "",
    @SerialName("doctorID")
    val id: String = "",
    @SerialName("fname")
    val firstName: String = "",
    @SerialName("lname")
    val lastName: String = "",
    @Transient
    val name: String = "$firstName $lastName",
    val clinicId: Long = 0,
    val email: String = "",
    @SerialName("phoneNumber")
    val phone: String = "",
    @SerialName("specialization")
    val specialty: String = "",
    val fromTime: Long = System.currentTimeMillis(),
    val toTime: Long = fromTime.plus(other = 5 * 60 * 60 * 1000),
    @Transient
    val availability: String = "${fromTime.getFormattedTime()} - ${toTime.getFormattedTime()}",
    @Transient
    val availableSlots: List<TimeSlot> =
        TimeSlot(startTime = fromTime, endTime = toTime).divide(
            slotDurationMinutes = 30,
        ),
    val profilePicture: String = "",
)
