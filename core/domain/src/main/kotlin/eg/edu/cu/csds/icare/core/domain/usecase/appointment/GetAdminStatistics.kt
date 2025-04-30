package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAdminStatistics(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(): Flow<Resource<AdminStatistics>> = repository.getAdminStatistics()
}
