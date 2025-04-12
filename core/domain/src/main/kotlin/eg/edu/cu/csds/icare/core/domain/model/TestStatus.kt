package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TestStatus(
    val id: Short = 0,
    val name: String = "",
    val nameAr: String = "",
)
