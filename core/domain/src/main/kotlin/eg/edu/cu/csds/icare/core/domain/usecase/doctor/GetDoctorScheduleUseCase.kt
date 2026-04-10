package eg.edu.cu.csds.icare.core.domain.usecase.doctor

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow

class GetDoctorScheduleUseCase(
    private val repository: ClinicsRepository,
) {
    operator fun invoke(uid: String?): Flow<RequestState<DoctorSchedule, DataError.Remote>> =
        repository.getDoctorSchedule(uid)
}
