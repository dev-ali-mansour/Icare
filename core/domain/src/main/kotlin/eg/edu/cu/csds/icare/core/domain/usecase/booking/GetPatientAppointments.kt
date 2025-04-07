package eg.edu.cu.csds.icare.core.domain.usecase.booking

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow

class GetPatientAppointments(
    private val repository: BookingRepository,
) {
    operator fun invoke(): Flow<List<Appointment>> = repository.getPatientAppointments()
}
