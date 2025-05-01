package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class UpdateAppointment(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(appointment: Appointment): Flow<Resource<Nothing?>> = repository.updateAppointment(appointment)
}
