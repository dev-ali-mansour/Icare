package eg.edu.cu.csds.icare.core.domain.usecase.center.staff

import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class ListCenterStaff(
    private val repository: CentersRepository,
) {
    operator fun invoke(centerId: Long): Flow<Resource<List<CenterStaff>>> = repository.listCenterStaff(centerId)
}
