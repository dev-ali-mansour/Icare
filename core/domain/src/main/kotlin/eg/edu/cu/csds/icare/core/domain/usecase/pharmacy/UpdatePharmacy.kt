package eg.edu.cu.csds.icare.core.domain.usecase.pharmacy

import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class UpdatePharmacy(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(pharmacy: Pharmacy): Flow<Resource<Nothing?>> = repository.updatePharmacy(pharmacy)
}
