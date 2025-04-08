package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow

class RescheduleAppointment(
    private val repository: BookingRepository,
) {
    operator fun invoke(
        appointmentId: Long,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> = repository.rescheduleAppointment(appointmentId, dateTime)
}
