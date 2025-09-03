package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.R

sealed class AppointmentStatus(
    val code: Short,
    @param:StringRes
    val textResId: Int,
) {
    data object PendingStatus : AppointmentStatus(code = 1, textResId = R.string.core_ui_pending)

    data object ConfirmedStatus : AppointmentStatus(code = 2, textResId = R.string.core_ui_confirmed)

    data object CompletedStatus : AppointmentStatus(code = 3, textResId = R.string.core_ui_completed)

    data object CancelledStatus : AppointmentStatus(code = 4, textResId = R.string.core_ui_cancelled)
}
