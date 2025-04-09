package eg.edu.cu.csds.icare.core.domain.usecase.center.staff

import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class AddNewCenterStaff(
    private val repository: CentersRepository,
) {
    operator fun invoke(staff: CenterStaff): Flow<Resource<Nothing?>> = repository.addNewCenterStaff(staff)
}
