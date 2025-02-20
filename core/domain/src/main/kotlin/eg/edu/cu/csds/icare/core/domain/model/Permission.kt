package eg.edu.cu.csds.icare.core.domain.model

sealed class Permission(
    val code: Short,
) {
    data object ViewMedicalHistoryPermission : Permission(1)

    data object CreatePrescriptionPermission : Permission(2)
}
