package eg.edu.cu.csds.icare.feature.admin.screen.pharmacy

data class PharmacyState(
    val isLoading: Boolean = false,
    val id: Long = 0,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val effect: PharmacyEffect? = null,
)
