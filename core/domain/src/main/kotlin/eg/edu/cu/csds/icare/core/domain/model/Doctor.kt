package eg.edu.cu.csds.icare.core.domain.model

import eg.edu.cu.csds.icare.core.domain.util.divide
import eg.edu.cu.csds.icare.core.domain.util.getFormattedTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Doctor(
    @Transient
    val token: String = "",
    @SerialName("doctorID")
    val id: String = "",
    @SerialName("fName")
    val firstName: String = "",
    @SerialName("lName")
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
    val rating: Double = 4.5,
    val profilePicture: String = "https://t4.ftcdn.net/jpg/01/98/82/75/360_F_198827520_wVNNHdMq4yLJe76WWivQQ5Ev2WtXac4N.webp",
)
