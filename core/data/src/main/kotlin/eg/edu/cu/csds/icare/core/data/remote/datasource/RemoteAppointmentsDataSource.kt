package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.AdminStatisticsDto
import eg.edu.cu.csds.icare.core.data.dto.AppointmentDto
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteAppointmentsDataSource {
    fun getPatientAppointments(): Flow<Resource<List<AppointmentDto>>>

    fun getAppointments(): Flow<Resource<List<AppointmentDto>>>

    fun getAppointments(statusId: Short): Flow<Resource<List<AppointmentDto>>>

    fun bookAppointment(appointment: AppointmentDto): Flow<Resource<Nothing?>>

    fun updateAppointment(appointment: AppointmentDto): Flow<Resource<Nothing?>>

    fun getAdminStatistics(): Flow<Resource<AdminStatisticsDto>>
}
