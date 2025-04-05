package eg.edu.cu.csds.icare.core.domain.usecase.booking

import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository

class RescheduleAppointment(private val repository: BookingRepository)  {

    suspend operator fun invoke(
        appointmentId: String,
        newDate: String,
        newTime: String
    ): Result<Unit> {
        return repository.rescheduleAppointment(appointmentId, newDate, newTime)

    }
}