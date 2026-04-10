package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class UpdateAppointmentUseCase(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(appointment: Appointment): Flow<RequestState<Unit, DataError.Remote>> =
        repository.updateAppointment(appointment)
}
