package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class AddNewDoctorUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(doctor: Doctor): Flow<Result<Unit, DataError.Remote>> =
        repository.addNewDoctor(doctor)
}
