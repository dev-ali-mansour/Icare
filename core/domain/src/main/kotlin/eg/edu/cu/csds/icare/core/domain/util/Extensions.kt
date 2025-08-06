package eg.edu.cu.csds.icare.core.domain.util

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.TimeSlot
import eg.edu.cu.csds.icare.core.domain.util.Constants.MAX_PARTS_LENGTH
import java.math.RoundingMode
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 8
private const val EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
private const val PASS_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

val String.isValidEmail: Boolean
    get() = this.isNotBlank() && Pattern.compile(EMAIL_PATTERN).matcher(this).matches()

val String.isValidPassword: Boolean
    get() =
        this.isNotBlank() &&
            this.length >= MIN_PASS_LENGTH &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()

fun String.hash(): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return bytes.joinToString(":") { "%02X".format(it) }
}

fun String.isValidDoubleInput(): Boolean {
    if (isEmpty()) return true
    if (this == "." || this == ",") return false

    val cleaned = this.replace(",", ".")
    if (cleaned.count { it == '.' } > 1) return false

    val parts = cleaned.split(".")
    return when (parts.size) {
        1 -> parts[0].length <= MAX_PARTS_LENGTH
        2 -> parts[0].length <= MAX_PARTS_LENGTH && parts[1].length <= 2
        else -> false
    }
}

fun Short.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Int.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Long.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Long.isTomorrow(): Boolean {
    val tomorrowCalendar = Calendar.getInstance()
    tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1)

    val appointmentCalendar = Calendar.getInstance()
    appointmentCalendar.timeInMillis = this

    return tomorrowCalendar.get(Calendar.YEAR) == appointmentCalendar.get(Calendar.YEAR) &&
        tomorrowCalendar.get(Calendar.MONTH) == appointmentCalendar.get(Calendar.MONTH) &&
        tomorrowCalendar.get(Calendar.DAY_OF_MONTH) == appointmentCalendar.get(Calendar.DAY_OF_MONTH)
}

fun Double.toFormattedString(): String =
    runCatching {
        DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.ENGLISH))
            .apply {
                roundingMode = RoundingMode.CEILING
            }.format(this)
    }.getOrElse { "" }

fun Double.formatForDisplay(): String = if (this == 0.0) "" else String.format(Locale.ENGLISH, "%.2f", this)

fun TimeSlot.divide(slotDurationMinutes: Short): List<TimeSlot> {
    require(slotDurationMinutes > 0) { "lengthMinutes was $slotDurationMinutes. Must specify positive amount of minutes." }

    val lengthMillis = slotDurationMinutes * Constants.MINUTES_TO_MILLIS
    val timeSlots = mutableListOf<TimeSlot>()
    var nextStartTime = startTime

    while (nextStartTime < endTime) {
        val nextEndTime = nextStartTime + lengthMillis
        val slotEndTime = if (nextEndTime > endTime) endTime else nextEndTime
        timeSlots.add(TimeSlot(nextStartTime, slotEndTime))
        nextStartTime = nextEndTime
    }

    return timeSlots
}

fun getNextDaysTimestamps(count: Int): List<Long> {
    val calendar = Calendar.getInstance()
    val timestamps = mutableListOf<Long>()

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    timestamps.add(calendar.timeInMillis)

    repeat(count) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        timestamps.add(calendar.timeInMillis)
    }
    return timestamps
}

fun getAvailableTimeSlots(
    from: Long,
    to: Long,
    bookedAppointments: List<Appointment>,
    slotPeriodMinutes: Int = 30,
    futureDaysCount: Int = 15,
): List<Long> {
    val fromTime = extractHourAndMinute(from)
    val toTime = extractHourAndMinute(to)
    val allAvailableSlots = mutableListOf<Long>()
    val slotPeriodMillis = TimeUnit.MINUTES.toMillis(slotPeriodMinutes.toLong())
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    repeat(futureDaysCount) {
        val currentDate = calendar.timeInMillis
        val dailyBookedAppointments =
            bookedAppointments.filter { appointment ->
                val appointmentCalendar = Calendar.getInstance()
                appointmentCalendar.timeInMillis = appointment.dateTime

                val appointmentDate =
                    appointmentCalendar
                        .apply {
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }.timeInMillis

                appointmentDate == currentDate
            }

        val dailyAvailableSlots =
            getDailyAvailableSlots(
                fromTime = fromTime,
                toTime = toTime,
                bookedAppointments = dailyBookedAppointments,
                slotPeriodMillis = slotPeriodMillis,
                currentDate = currentDate,
            )
        allAvailableSlots.addAll(dailyAvailableSlots)

        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return allAvailableSlots
}

private fun getDailyAvailableSlots(
    fromTime: Time,
    toTime: Time,
    bookedAppointments: List<Appointment>,
    slotPeriodMillis: Long,
    currentDate: Long,
): List<Long> {
    val availableSlots = mutableListOf<Long>()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentDate
    calendar.set(Calendar.HOUR_OF_DAY, fromTime.hour)
    calendar.set(Calendar.MINUTE, fromTime.minute)
    val fromTimestamp = calendar.timeInMillis
    calendar.set(Calendar.HOUR_OF_DAY, toTime.hour)
    calendar.set(Calendar.MINUTE, toTime.minute)
    val toTimestamp = calendar.timeInMillis

    var currentSlotStart = fromTimestamp
    while (currentSlotStart < toTimestamp) {
        val currentSlotEnd = currentSlotStart + slotPeriodMillis

        val isBooked =
            bookedAppointments.any { appointment ->
                val appointmentEnd = appointment.dateTime + slotPeriodMillis
                (currentSlotStart < appointmentEnd) && (currentSlotEnd > appointment.dateTime)
            }

        if (!isBooked && currentSlotStart >= Calendar.getInstance().timeInMillis) {
            availableSlots.add(currentSlotStart)
        }

        currentSlotStart += slotPeriodMillis
    }

    return availableSlots
}

data class Time(
    val hour: Int,
    val minute: Int,
)

private fun extractHourAndMinute(timestamp: Long): Time {
    val calendar = Calendar.getInstance(TimeZone.getDefault()) // Use the device's time zone
    calendar.timeInMillis = timestamp

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return Time(hour, minute)
}
