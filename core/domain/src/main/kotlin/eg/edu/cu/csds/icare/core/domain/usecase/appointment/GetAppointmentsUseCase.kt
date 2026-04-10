package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAppointmentsUseCase(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(): Flow<RequestState<List<Appointment>, DataError.Remote>> =
        repository.getAppointments()
}
