package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class BookAppointmentUseCase(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(appointment: Appointment): Flow<Result<Unit, DataError.Remote>> =
        repository.bookAppointment(appointment)
}
