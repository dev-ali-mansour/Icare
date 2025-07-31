package eg.edu.cu.csds.icare.core.domain.model

data class CenterStaff(
    val token: String = "",
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "$firstName $lastName",
    val centerId: Long = 0,
    val centerName: String = "",
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
)
