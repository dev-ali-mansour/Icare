package eg.edu.cu.csds.icare.admin.screen.center

data class CenterState(
    val isLoading: Boolean = false,
    val id: Long = 0,
    val name: String = "",
    val type: Short = 0,
    val isTypesExpanded: Boolean = false,
    val phone: String = "",
    val address: String = "",
    val effect: CenterEffect? = null,
)
