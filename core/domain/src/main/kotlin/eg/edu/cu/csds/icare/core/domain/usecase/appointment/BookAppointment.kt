package eg.edu.cu.csds.icare.core.domain.usecase.appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class BookAppointment(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(
        doctorId: Int,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> = repository.bookAppointment(doctorId, dateTime)
}
