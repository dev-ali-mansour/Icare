package eg.edu.cu.csds.icare.core.domain.model

import eg.edu.cu.csds.icare.core.domain.util.divide

data class Doctor(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "$firstName $lastName",
    val clinicId: Long = 0,
    val email: String = "",
    val phone: String = "",
    val specialty: String = "",
    val fromTime: Long = System.currentTimeMillis(),
    val toTime: Long = fromTime.plus(other = 5 * 60 * 60 * 1000),
    val availability: String = "",
    val availableSlots: List<TimeSlot> =
        TimeSlot(startTime = fromTime, endTime = toTime).divide(
            slotDurationMinutes = 30,
        ),
    val rating: Double = 0.0,
    val price: Double = 0.0,
    val profilePicture: String = "",
)
