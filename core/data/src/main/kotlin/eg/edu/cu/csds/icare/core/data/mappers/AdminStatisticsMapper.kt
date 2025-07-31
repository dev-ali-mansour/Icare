package eg.edu.cu.csds.icare.core.data.mappers

import eg.edu.cu.csds.icare.core.data.dto.AdminStatisticsDto
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics

fun AdminStatisticsDto.toAdminStatistics(): AdminStatistics =
    AdminStatistics(
        totalUsers = this.totalUsers,
        doctors = this.doctors,
        pharmacies = this.pharmacies,
        scanCenters = this.scanCenters,
        labCenters = this.labCenters,
        pending = this.pending,
        confirmed = this.confirmed,
        completed = this.completed,
        cancelled = this.cancelled,
    )
