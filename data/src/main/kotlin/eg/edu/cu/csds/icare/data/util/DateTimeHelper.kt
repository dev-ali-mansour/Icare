package eg.edu.cu.csds.icare.data.util

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.getFormattedDate(
    context: Context,
    pattern: String = "dd/MM/yyyy",
): String = getFormattedDateTime(context, pattern)

fun Long.getFormattedTime(
    context: Context,
    pattern: String = "hh:mm a",
): String = getFormattedDateTime(context, pattern)

fun Long.getFormattedDateTime(
    context: Context,
    pattern: String = "dd/MM/yyyy hh:mm a",
): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    // Get the current locale from the application's configuration
    val configuration = context.resources.configuration
    val locale = ConfigurationCompat.getLocales(configuration)[0] ?: Locale.getDefault()
    val dateFormat = SimpleDateFormat(pattern, locale)
    return dateFormat.format(calendar.time)
}
