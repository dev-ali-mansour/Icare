package eg.edu.cu.csds.icare.core.domain.model

import eg.edu.cu.csds.icare.core.domain.util.divide
import eg.edu.cu.csds.icare.core.domain.util.getFormattedTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Doctor(
    @Transient
    val token: String = "",
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    @Transient
    val name: String = "$firstName $lastName",
    val clinicId: Long = 0,
    val email: String = "",
    val phone: String = "",
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
