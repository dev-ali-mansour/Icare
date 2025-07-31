package eg.edu.cu.csds.icare.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdminStatisticsDto(
    val totalUsers: Long = 0,
    val doctors: Long = 0,
    val pharmacies: Long = 0,
    val scanCenters: Long = 0,
    val labCenters: Long = 0,
    val pending: Long = 0,
    val confirmed: Long = 0,
    val completed: Long = 0,
    val cancelled: Long = 0,
)
