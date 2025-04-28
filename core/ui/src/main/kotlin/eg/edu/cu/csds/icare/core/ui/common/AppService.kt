package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.navigation.Screen

sealed class AppService(
    @DrawableRes
    val iconResId: Int,
    @StringRes
    val textResId: Int,
    val screen: Screen,
) {
    data object ScanCenter : AppService(
        iconResId = R.drawable.ic_scan,
        textResId = R.string.scan_center,
        screen = Screen.ScanCenters,
    )

    data object BookAppointment : AppService(
        iconResId = R.drawable.ic_appointment,
        textResId = R.string.book_appointment,
        screen = Screen.DoctorList,
    )

    data object LabCenter : AppService(
        iconResId = R.drawable.ic_lab_colored,
        textResId = R.string.lab_center,
        screen = Screen.LabCenters,
    )

    data object Pharmacy : AppService(
        iconResId = R.drawable.ic_pharmacy_colored,
        textResId = R.string.pharmacies,
        screen = Screen.Pharmacies,
    )
}
