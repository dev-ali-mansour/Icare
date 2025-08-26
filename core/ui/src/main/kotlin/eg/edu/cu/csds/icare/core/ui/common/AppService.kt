package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.R
import eg.edu.cu.csds.icare.core.ui.navigation.Route

sealed class AppService(
    @param:DrawableRes
    val iconResId: Int,
    @param:StringRes
    val textResId: Int,
    val route: Route,
) {
    data object ScanCenter : AppService(
        iconResId = R.drawable.ic_scan,
        textResId = R.string.scan_center,
        route = Route.ScanCenters,
    )

    data object BookAppointment : AppService(
        iconResId = R.drawable.ic_appointment,
        textResId = R.string.appointment_booking,
        route = Route.DoctorList,
    )

    data object LabCenter : AppService(
        iconResId = R.drawable.ic_lab_colored,
        textResId = R.string.lab_center,
        route = Route.LabCenters,
    )

    data object Pharmacy : AppService(
        iconResId = R.drawable.ic_pharmacy_colored,
        textResId = R.string.pharmacies,
        route = Route.Pharmacies,
    )
}
