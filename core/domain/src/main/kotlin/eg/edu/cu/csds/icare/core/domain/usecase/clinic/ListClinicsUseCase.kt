package eg.edu.cu.csds.icare.core.domain.usecase.clinic

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class ListClinicsUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(forceUpdate: Boolean = false): Flow<RequestState<List<Clinic>, DataError.Remote>> =
        repository.listClinics(forceUpdate)
}
