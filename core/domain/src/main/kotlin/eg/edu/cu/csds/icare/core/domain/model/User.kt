package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class User(
    val userId: String = "",
    @SerialName("roleID")
    val roleId: Short = 6,
    val nationalId: String = "",
    val birthDate: Long = 0,
    val gender: Char = 'M',
    val address: String = "",
    val phoneNumber: String = "",
    val isActive: Boolean = true,
    @Transient
    val displayName: String = "",
    @Transient
    val email: String = "",
    @Transient
    val photoUrl: String = "",
    @Transient
    val isEmailVerified: Boolean = false,
    @Transient
    val linkedWithGoogle: Boolean = false,
)
