package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.BookingMethod
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import eg.edu.cu.csds.icare.data.local.datasource.LocalAppointmentsDataSource
import eg.edu.cu.csds.icare.data.local.db.entity.toEntity
import eg.edu.cu.csds.icare.data.local.db.entity.toModel
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteAppointmentsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class AppointmentsRepositoryImpl(
    private val remoteAppointmentsDataSource: RemoteAppointmentsDataSource,
    private val localAppointmentsDataSource: LocalAppointmentsDataSource,
) : AppointmentsRepository {
    override fun listBookingMethods(forceRefresh: Boolean): Flow<Resource<List<BookingMethod>>> =
        flow {
            if (!forceRefresh) {
                localAppointmentsDataSource
                    .listBookingMethods()
                    .distinctUntilChanged()
                    .collect { methods ->
                        emit(Resource.Success(data = methods.map { it.toModel() }))
                    }
                return@flow
            }
            remoteAppointmentsDataSource.fetchBookingMethods().map { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success -> {
                        res.data?.let { methods ->
                            localAppointmentsDataSource.persistBookingMethods(methods.map { it.toEntity() })
                        }
                        localAppointmentsDataSource
                            .listBookingMethods()
                            .distinctUntilChanged()
                            .collect { methods ->
                                emit(Resource.Success(data = methods.map { it.toModel() }))
                            }
                    }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun getPatientAppointments(): Flow<Resource<List<Appointment>>> = remoteAppointmentsDataSource.getPatientAppointments()

    override fun getAppointments(): Flow<Resource<List<Appointment>>> = remoteAppointmentsDataSource.getAppointments()

    override fun getAppointments(statusId: Short): Flow<Resource<List<Appointment>>> =
        remoteAppointmentsDataSource.getAppointments(statusId)

    override fun bookAppointment(
        doctorId: Int,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> = remoteAppointmentsDataSource.bookAppointment(doctorId, dateTime)

    override fun updateAppointmentStatus(
        appointmentId: Long,
        statusId: Short,
    ): Flow<Resource<Nothing?>> = remoteAppointmentsDataSource.updateAppointmentStatus(appointmentId, statusId)

    override fun rescheduleAppointment(
        appointmentId: Long,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> = rescheduleAppointment(appointmentId, dateTime)
}
