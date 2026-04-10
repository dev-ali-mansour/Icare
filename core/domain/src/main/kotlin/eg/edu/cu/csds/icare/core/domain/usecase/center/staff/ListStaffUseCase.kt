package eg.edu.cu.csds.icare.core.domain.usecase.center.staff

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.model.Staff
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class ListStaffUseCase(
    private val repository: CentersRepository,
) {
    operator fun invoke(): Flow<RequestState<List<Staff>, DataError.Remote>> = repository.listCenterStaff()
}
