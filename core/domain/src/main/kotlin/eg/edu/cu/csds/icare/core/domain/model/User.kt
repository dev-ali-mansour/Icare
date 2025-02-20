package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val roleId: Short = 100,
    val job: String = "",
    val nationalId: String = "",
    val dateOfBirth: Long = 0,
    val gender: Char = 'M',
    val address: String = "",
    val phoneNumber: String = "",
    val permissions: List<Short> = listOf(),
)
