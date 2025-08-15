package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class GetDoctorScheduleUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(uid: String): Flow<Result<DoctorSchedule, DataError.Remote>> =
        repository.getDoctorSchedule(uid)
}
