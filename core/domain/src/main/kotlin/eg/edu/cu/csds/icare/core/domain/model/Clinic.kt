package eg.edu.cu.csds.icare.core.domain.model

data class Clinic(
    val id: Long = 0,
    val name: String = "",
    val type: String = "",
    val phone: String = "",
    val address: String = "",
    val isOpen: Boolean = false,
)
