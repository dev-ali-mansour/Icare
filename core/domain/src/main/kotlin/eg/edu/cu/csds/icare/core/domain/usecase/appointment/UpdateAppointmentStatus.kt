package eg.edu.cu.csds.icare.core.domain.usecase.appointment

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow

class UpdateAppointmentStatus(
    private val repository: BookingRepository,
) {
    operator fun invoke(
        appointmentId: Long,
        statusId: Short,
    ): Flow<Resource<Nothing?>> = repository.updateAppointmentStatus(appointmentId, statusId)
}
