package eg.edu.cu.csds.icare.core.domain.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Patterns
import java.math.RoundingMode
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

fun Short.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Int.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Long.toFormattedString(): String = DecimalFormat("#,###", DecimalFormatSymbols(Locale.ENGLISH)).format(this)

fun Long.getFormattedDate(pattern: String = "dd/MM/yyyy"): String = getFormattedDateTime(pattern)

fun Long.getFormattedDateTime(pattern: String = "dd/MM/yyyy HH:mm a"): String {
    val calender = Calendar.getInstance()
    calender.timeInMillis = this
    val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    return dateFormat.format(calender.timeInMillis)
}

fun Double.toFormattedString(): String =
    DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.ENGLISH))
        .apply {
            roundingMode = RoundingMode.CEILING
        }.format(this)
