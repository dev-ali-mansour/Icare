package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.mappers.toAdminStatistics
import eg.edu.cu.csds.icare.core.data.mappers.toAppointment
import eg.edu.cu.csds.icare.core.data.mappers.toAppointmentDto
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteAppointmentsDataSource
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class AppointmentsRepositoryImpl(
    private val remoteAppointmentsDataSource: RemoteAppointmentsDataSource,
) : AppointmentsRepository {
    override fun getPatientAppointments(): Flow<Resource<List<Appointment>>> =
        flow {
            remoteAppointmentsDataSource.getPatientAppointments().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toAppointment() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun getAppointments(): Flow<Resource<List<Appointment>>> =
        flow {
            remoteAppointmentsDataSource.getAppointments().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toAppointment() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun getAppointments(statusId: Short): Flow<Resource<List<Appointment>>> =
        flow {
            remoteAppointmentsDataSource.getAppointments(statusId).collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { doctors ->
                            emit(Resource.Success(data = doctors.map { it.toAppointment() }))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }

    override fun bookAppointment(appointment: Appointment): Flow<Resource<Nothing?>> =
        remoteAppointmentsDataSource.bookAppointment(appointment.toAppointmentDto())

    override fun updateAppointment(appointment: Appointment): Flow<Resource<Nothing?>> =
        remoteAppointmentsDataSource.updateAppointment(appointment.toAppointmentDto())

    override fun getAdminStatistics(): Flow<Resource<AdminStatistics>> =
        flow {
            remoteAppointmentsDataSource.getAdminStatistics().collect { res ->
                when (res) {
                    is Resource.Unspecified -> emit(Resource.Unspecified())

                    is Resource.Loading -> emit(Resource.Loading())

                    is Resource.Success ->
                        res.data?.let { statisticsDto ->
                            emit(Resource.Success(data = statisticsDto.toAdminStatistics()))
                        }

                    is Resource.Error -> emit(Resource.Error(res.error))
                }
            }
        }
}
