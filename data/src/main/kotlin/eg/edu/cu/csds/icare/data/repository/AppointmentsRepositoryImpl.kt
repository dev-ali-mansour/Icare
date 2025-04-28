package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import eg.edu.cu.csds.icare.data.remote.datasource.RemoteAppointmentsDataSource
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AppointmentsRepositoryImpl(
    private val remoteAppointmentsDataSource: RemoteAppointmentsDataSource,
) : AppointmentsRepository {
    override fun getPatientAppointments(): Flow<Resource<List<Appointment>>> = remoteAppointmentsDataSource.getPatientAppointments()

    override fun getAppointments(): Flow<Resource<List<Appointment>>> = remoteAppointmentsDataSource.getAppointments()

    override fun getAppointments(statusId: Short): Flow<Resource<List<Appointment>>> =
        remoteAppointmentsDataSource.getAppointments(statusId)

    override fun bookAppointment(appointment: Appointment): Flow<Resource<Nothing?>> =
        remoteAppointmentsDataSource.bookAppointment(appointment)

    override fun updateAppointment(appointment: Appointment): Flow<Resource<Nothing?>> =
        remoteAppointmentsDataSource.updateAppointment(appointment)
}
