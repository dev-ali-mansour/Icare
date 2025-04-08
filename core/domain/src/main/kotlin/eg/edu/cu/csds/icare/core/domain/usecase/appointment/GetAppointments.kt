package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAppointments(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(): Flow<List<Appointment>> = repository.getAppointments()
}
