package eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class BookAppointment(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(appointment: Appointment): Flow<Resource<Nothing?>> = repository.bookAppointment(appointment)
}
