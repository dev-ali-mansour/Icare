package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class AddNewDoctorUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(doctor: Doctor): Flow<RequestState<Unit, DataError.Remote>> =
        repository.addNewDoctor(doctor)
}
