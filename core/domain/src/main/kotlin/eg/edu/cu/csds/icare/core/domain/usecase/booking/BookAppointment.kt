package eg.edu.cu.csds.icare.core.domain.usecase.booking

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository

class BookAppointment(private val repository: BookingRepository) {

    suspend operator fun invoke(appointment: Appointment): Result<Unit> {
        return repository.bookAppointment(appointment)
    }



}