package eg.edu.cu.csds.icare.core.domain.model

data class ClinicStaff(
    val token: String = "",
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "$firstName $lastName",
    val clinicId: Long = 0,
    val clinicName: String = "",
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
)
