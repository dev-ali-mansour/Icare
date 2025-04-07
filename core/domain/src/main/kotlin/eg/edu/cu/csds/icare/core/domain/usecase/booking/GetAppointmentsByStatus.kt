package eg.edu.cu.csds.icare.core.domain.usecase.booking

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow

class GetAppointmentsByStatus(private val repository: BookingRepository)  {
    operator fun invoke(statusId: Short): Flow<List<Appointment>> {
        return repository.getAppointments(statusId)
    }


}