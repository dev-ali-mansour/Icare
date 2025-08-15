package eg.edu.cu.csds.icare.core.domain.usecase.pharmacist

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class UpdatePharmacistUseCase(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(pharmacist: Pharmacist): Flow<Result<Unit, DataError.Remote>> =
        repository.updatePharmacist(pharmacist)
}
