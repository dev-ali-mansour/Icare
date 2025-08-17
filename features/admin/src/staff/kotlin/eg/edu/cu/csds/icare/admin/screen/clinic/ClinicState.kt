package eg.edu.cu.csds.icare.admin.screen.clinic

data class ClinicState(
    val isLoading: Boolean = false,
    val id: Long = 0,
    val name: String = "",
    val type: String = "",
    val phone: String = "",
    val address: String = "",
    val isOpen: Boolean = true,
)
