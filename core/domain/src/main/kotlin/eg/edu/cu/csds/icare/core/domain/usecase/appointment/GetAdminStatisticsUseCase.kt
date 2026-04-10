package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAdminStatisticsUseCase(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(): Flow<RequestState<AdminStatistics, DataError.Remote>> =
        repository.getAdminStatistics()
}
