package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.BookingRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalSettingsDataSource
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single



@Single
class BookingRepositoryImpl(val bookingDataSource: BookingDataSource) {
}