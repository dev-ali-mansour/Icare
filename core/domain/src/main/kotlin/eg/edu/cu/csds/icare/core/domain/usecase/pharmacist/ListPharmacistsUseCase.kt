package eg.edu.cu.csds.icare.core.domain.usecase.pharmacist

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.PharmaciesRepository
import kotlinx.coroutines.flow.Flow

class ListPharmacistsUseCase(
    private val repository: PharmaciesRepository,
) {
    operator fun invoke(): Flow<Result<List<Pharmacist>, DataError.Remote>> = repository.listPharmacists()
}
