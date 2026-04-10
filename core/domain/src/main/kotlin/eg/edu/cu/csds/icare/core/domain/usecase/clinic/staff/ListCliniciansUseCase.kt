package eg.edu.cu.csds.icare.core.domain.usecase.clinic.staff

import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class ListCliniciansUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(): Flow<RequestState<List<Clinician>, DataError.Remote>> = repository.listClinicians()
}
