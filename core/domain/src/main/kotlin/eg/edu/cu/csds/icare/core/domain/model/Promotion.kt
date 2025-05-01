package eg.edu.cu.csds.icare.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Promotion(
    val id: Short = 0,
    val imageUrl: String = "",
    val discount: String = "",
    val description: String = "",
)
