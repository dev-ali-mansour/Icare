package eg.edu.cu.csds.icare.core.domain.usecase.booking

import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository

class CancelAppointment(private val repository: BookingRepository)  {

    suspend operator fun invoke(appointmentId: String): Result<Unit> {
        return repository.cancelAppointment(appointmentId)
    }

}