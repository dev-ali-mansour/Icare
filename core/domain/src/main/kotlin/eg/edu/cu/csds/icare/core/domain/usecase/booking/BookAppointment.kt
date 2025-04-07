package eg.edu.cu.csds.icare.core.domain.usecase.booking
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow

class BookAppointment(private val repository: BookingRepository) {

    operator fun invoke(doctorId: Int, dateTime: Long): Flow<Resource<Nothing?>> {
        return repository.bookAppointment(doctorId, dateTime)
    }
}