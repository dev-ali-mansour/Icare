package eg.edu.cu.csds.icare.core.domain.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Patterns
import eg.edu.cu.csds.icare.core.domain.model.TimeSlot
import java.math.RoundingMode
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 8
private const val PASS_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

val String.isValidEmail: Boolean
    get() = this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

val String.isValidPassword: Boolean
    get() =
        this.isNotBlank() &&
            this.length >= MIN_PASS_LENGTH &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()

fun String.toBitmap(): Bitmap? =
    runCatching {
        val decodedBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }.getOrNull()

fun String.hash(): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return bytes.joinToString(":") { "%02X".format(it) }
}

fun Short.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Int.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Long.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Long.getFormattedDate(pattern: String = "dd/MM/yyyy"): String = getFormattedDateTime(pattern)

fun Long.getFormattedTime(pattern: String = "hh:mm a"): String = getFormattedDateTime(pattern)

fun Long.getFormattedDateTime(pattern: String = "dd/MM/yyyy hh:mm a"): String {
    val calender = Calendar.getInstance()
    calender.timeInMillis = this
    val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    return dateFormat.format(calender.timeInMillis)
}

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
    DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.ENGLISH))
        .apply {
            roundingMode = RoundingMode.CEILING
        }.format(this)

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
