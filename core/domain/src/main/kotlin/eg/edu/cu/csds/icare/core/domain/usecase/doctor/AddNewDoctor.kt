package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class AddNewDoctor(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(doctor: Doctor): Flow<Resource<Nothing?>> = repository.addNewDoctor(doctor)
}
