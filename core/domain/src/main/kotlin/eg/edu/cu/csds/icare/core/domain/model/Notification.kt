package eg.edu.cu.csds.icare.core.domain.model

import java.util.Date

data class Notification(
    val id: Long,
    val text: String,
    val date: Date,
)
