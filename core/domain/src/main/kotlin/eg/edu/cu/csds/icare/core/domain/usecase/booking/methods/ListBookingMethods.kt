package eg.edu.cu.csds.icare.core.domain.usecase.booking.methods

import eg.edu.cu.csds.icare.core.domain.model.BookingMethod
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow

class ListBookingMethods(
    private val repository: AppointmentsRepository,
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<Resource<List<BookingMethod>>> = repository.listBookingMethods(forceRefresh)
}
