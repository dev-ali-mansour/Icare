package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAdminStatisticsUseCase(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(): Flow<Result<AdminStatistics, DataError.Remote>> = repository.getAdminStatistics()
}
