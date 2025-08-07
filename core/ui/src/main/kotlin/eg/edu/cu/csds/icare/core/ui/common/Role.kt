package eg.edu.cu.csds.icare.core.ui.common

import androidx.annotation.StringRes
import eg.edu.cu.csds.icare.core.ui.R

sealed class Role(
    val code: Short,
    @param:StringRes
    val name: Int,
) {
    data object AdminRole : Role(code = 1, name = R.string.admins)

    data object DoctorRole : Role(code = 2, name = R.string.doctors)

    data object ClinicStaffRole : Role(code = 3, name = R.string.clinic_staffs)

    data object PharmacistRole : Role(code = 4, name = R.string.pharmacists)

    data object CenterStaffRole : Role(code = 5, name = R.string.center_staffs)

    data object PatientRole : Role(code = 6, name = R.string.patients)
}
