package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAppointmentsByStatus(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(statusId: Short): Flow<List<Appointment>> = repository.getAppointments(statusId)
}
