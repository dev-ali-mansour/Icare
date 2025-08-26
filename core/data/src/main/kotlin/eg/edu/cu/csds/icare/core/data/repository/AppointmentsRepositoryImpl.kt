package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.mappers.toAdminStatistics
import eg.edu.cu.csds.icare.core.data.mappers.toAppointment
import eg.edu.cu.csds.icare.core.data.mappers.toAppointmentDto
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteAppointmentsDataSource
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import eg.edu.cu.csds.icare.core.domain.model.onError
import eg.edu.cu.csds.icare.core.domain.model.onSuccess
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class AppointmentsRepositoryImpl(
    private val remoteAppointmentsDataSource: RemoteAppointmentsDataSource,
) : AppointmentsRepository {
    override fun getPatientAppointments(): Flow<Result<List<Appointment>, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getPatientAppointments()
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(Result.Success(data = entities.map { it.toAppointment() }))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun getAppointments(): Flow<Result<List<Appointment>, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getAppointments()
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(Result.Success(data = entities.map { it.toAppointment() }))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun getAppointments(statusId: Short): Flow<Result<List<Appointment>, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getAppointments(statusId)
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(Result.Success(data = entities.map { it.toAppointment() }))
                        }.onError { emit(Result.Error(it)) }
                }
        }

    override fun bookAppointment(appointment: Appointment): Flow<Result<Unit, DataError.Remote>> =
        remoteAppointmentsDataSource.bookAppointment(appointment.toAppointmentDto())

    override fun updateAppointment(appointment: Appointment): Flow<Result<Unit, DataError.Remote>> =
        remoteAppointmentsDataSource.updateAppointment(appointment.toAppointmentDto())

    override fun getAdminStatistics(): Flow<Result<AdminStatistics, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getAdminStatistics()
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(Result.Success(data = entities.toAdminStatistics()))
                        }.onError { emit(Result.Error(it)) }
                }
        }
}
