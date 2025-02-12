package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = 0,
    val roleId: Short = 0,
    val phoneNumber: String = "",
    val dateOfBirth: Long = System.currentTimeMillis(),
    val gender: Char = 'M',
    val address: String = "",
    val nationalId: String = "",
    val photoPath: String = "",
    val permissions: List<Short> = listOf(),
)
