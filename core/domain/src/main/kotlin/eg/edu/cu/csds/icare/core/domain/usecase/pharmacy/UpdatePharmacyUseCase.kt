package eg.edu.cu.csds.icare.core.domain.usecase.pharmacy

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class UpdatePharmacyUseCase(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(pharmacy: Pharmacy): Flow<RequestState<Unit, DataError.Remote>> =
        repository.updatePharmacy(pharmacy)
}
