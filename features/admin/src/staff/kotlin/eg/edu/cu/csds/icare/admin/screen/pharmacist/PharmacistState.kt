package eg.edu.cu.csds.icare.admin.screen.pharmacist

data class PharmacistState(
    val isLoading: Boolean = false,
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
