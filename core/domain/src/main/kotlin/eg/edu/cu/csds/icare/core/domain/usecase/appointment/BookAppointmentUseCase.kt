package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class BookAppointmentUseCase(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(
        doctorId: String,
        dateTime: Long,
    ): Flow<RequestState<Unit, DataError.Remote>> = repository.bookAppointment(doctorId, dateTime)
}
