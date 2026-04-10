package eg.edu.cu.csds.icare.core.domain.usecase.pharmacist

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class UpdatePharmacistUseCase(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(pharmacist: Pharmacist): Flow<RequestState<Unit, DataError.Remote>> =
        repository.updatePharmacist(pharmacist)
}
