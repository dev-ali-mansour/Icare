package eg.edu.cu.csds.icare.core.domain.model

data class User(
    val userId: String = "",
    val roleId: Short = 6,
    val nationalId: String = "",
    val birthDate: Long = 0,
    val gender: String = "M",
    val address: String = "",
    val phoneNumber: String = "",
    val isActive: Boolean = true,
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val isEmailVerified: Boolean = false,
    val linkedWithGoogle: Boolean = false,
)
