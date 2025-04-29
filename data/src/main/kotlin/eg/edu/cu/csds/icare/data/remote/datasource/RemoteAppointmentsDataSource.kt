package eg.edu.cu.csds.icare.data.remote.datasource

import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteAppointmentsDataSource {
    fun getPatientAppointments(): Flow<Resource<List<Appointment>>>

    fun getAppointments(): Flow<Resource<List<Appointment>>>

    fun getAppointments(statusId: Short): Flow<Resource<List<Appointment>>>

    fun bookAppointment(appointment: Appointment): Flow<Resource<Nothing?>>

    fun updateAppointment(appointment: Appointment): Flow<Resource<Nothing?>>

    fun getAdminStatistics(): Flow<Resource<AdminStatistics>>
}
