package eg.edu.cu.csds.icare.core.domain.usecase.center

import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.CentersRepository
import kotlinx.coroutines.flow.Flow

class AddNewCenter(
    private val repository: CentersRepository,
) {
    operator fun invoke(center: LabImagingCenter): Flow<Resource<Nothing?>> = repository.addNewCenter(center)
}
