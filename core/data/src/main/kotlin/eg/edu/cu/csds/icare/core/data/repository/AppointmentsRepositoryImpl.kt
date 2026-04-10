package eg.edu.cu.csds.icare.core.data.repository

import eg.edu.cu.csds.icare.core.data.mappers.toAdminStatistics
import eg.edu.cu.csds.icare.core.data.mappers.toAppointment
import eg.edu.cu.csds.icare.core.data.mappers.toAppointmentDto
import eg.edu.cu.csds.icare.core.data.remote.datasource.RemoteAppointmentsDataSource
import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import eg.edu.cu.csds.icare.core.domain.util.onError
import eg.edu.cu.csds.icare.core.domain.util.onSuccess
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class AppointmentsRepositoryImpl(
    private val remoteAppointmentsDataSource: RemoteAppointmentsDataSource,
) : AppointmentsRepository {
    override fun getPatientAppointments(): Flow<RequestState<List<Appointment>, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getPatientAppointments()
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(RequestState.Success(data = entities.map { it.toAppointment() }))
                        }.onError { emit(RequestState.Error(it)) }
                }
        }

    override fun getAppointments(): Flow<RequestState<List<Appointment>, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getAppointments()
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(RequestState.Success(data = entities.map { it.toAppointment() }))
                        }.onError { emit(RequestState.Error(it)) }
                }
        }

    override fun getAppointments(statusId: Short): Flow<RequestState<List<Appointment>, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getAppointments(statusId)
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(RequestState.Success(data = entities.map { it.toAppointment() }))
                        }.onError { emit(RequestState.Error(it)) }
                }
        }

    override fun bookAppointment(
        doctorId: String,
        dateTime: Long,
    ): Flow<RequestState<Unit, DataError.Remote>> =
        remoteAppointmentsDataSource.bookAppointment(doctorId, dateTime)

    override fun updateAppointment(appointment: Appointment): Flow<RequestState<Unit, DataError.Remote>> =
        remoteAppointmentsDataSource.updateAppointment(appointment.toAppointmentDto())

    override fun getAdminStatistics(): Flow<RequestState<AdminStatistics, DataError.Remote>> =
        flow {
            remoteAppointmentsDataSource
                .getAdminStatistics()
                .collect { result ->
                    result
                        .onSuccess { entities ->
                            emit(RequestState.Success(data = entities.toAdminStatistics()))
                        }.onError { emit(RequestState.Error(it)) }
                }
        }
}
