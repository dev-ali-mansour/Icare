package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAppointmentsByStatusUseCase(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(statusId: Short): Flow<Result<List<Appointment>, DataError.Remote>> =
        repository.getAppointments(statusId)
}
