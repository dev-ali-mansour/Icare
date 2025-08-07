package eg.edu.cu.csds.icare.core.domain.model

data class Pharmacist(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val name: String = "$firstName $lastName",
    val pharmacyId: Long = 0,
    val pharmacyName: String = "",
    val email: String = "",
    val phone: String = "",
    val profilePicture: String = "",
)
