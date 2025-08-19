package eg.edu.cu.csds.icare.admin.screen.pharmacy

data class PharmacyState(
    val isLoading: Boolean = false,
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
)
