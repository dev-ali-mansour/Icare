package eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class GetAppointments(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(): Flow<Resource<List<Appointment>>> = repository.getAppointments()
}
