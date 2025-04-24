package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val roleId: Short = 6,
    val nationalId: String = "",
    val birthDate: Long = 0,
    val gender: Char = 'M',
    val address: String = "",
    val phoneNumber: String = "",
    val isActive: Boolean = true,
)
