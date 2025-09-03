package eg.edu.cu.csds.icare.core.data.util

sealed class Role(
    val code: Short,
) {
    object AdminRole : Role(code = 1)

    object DoctorRole : Role(code = 2)

    object ClinicianRole : Role(code = 3)

    object PharmacistRole : Role(code = 4)

    object CenterStaffRole : Role(code = 5)

    object PatientRole : Role(code = 6)
}
