package eg.edu.cu.csds.icare.core.domain.usecase.booking.appointment

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class UpdateAppointmentStatus(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(
        appointmentId: Long,
        statusId: Short,
    ): Flow<Resource<Nothing?>> = repository.updateAppointmentStatus(appointmentId, statusId)
}
