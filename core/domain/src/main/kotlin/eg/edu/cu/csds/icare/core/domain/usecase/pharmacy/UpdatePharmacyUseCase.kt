package eg.edu.cu.csds.icare.core.domain.usecase.pharmacy

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class UpdatePharmacyUseCase(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(pharmacy: Pharmacy): Flow<Result<Unit, DataError.Remote>> =
        repository.updatePharmacy(pharmacy)
}
