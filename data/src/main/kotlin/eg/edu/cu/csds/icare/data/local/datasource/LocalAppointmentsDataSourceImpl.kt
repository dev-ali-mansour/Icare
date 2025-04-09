package eg.edu.cu.csds.icare.data.local.datasource

import eg.edu.cu.csds.icare.data.local.db.dao.BookingMethodDao
import eg.edu.cu.csds.icare.data.local.db.entity.BookingMethodEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalAppointmentsDataSourceImpl(
    private val bookingMethodDao: BookingMethodDao,
) : LocalAppointmentsDataSource {
    override suspend fun persistBookingMethods(methods: List<BookingMethodEntity>) {
        bookingMethodDao.persistBookingMethods(methods)
    }

    override suspend fun listBookingMethods(): Flow<List<BookingMethodEntity>> = bookingMethodDao.listBookingMethods()
}
