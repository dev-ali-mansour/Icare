package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.entity.BookingMethodEntity
import kotlinx.coroutines.flow.Flow

interface LocalAppointmentsDataSource {
    suspend fun persistBookingMethods(methods: List<BookingMethodEntity>)

    suspend fun listBookingMethods(): Flow<List<BookingMethodEntity>>
}
