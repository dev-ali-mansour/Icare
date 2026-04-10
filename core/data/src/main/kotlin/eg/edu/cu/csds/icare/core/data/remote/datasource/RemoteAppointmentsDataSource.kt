package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.AdminStatisticsDto
import eg.edu.cu.csds.icare.core.data.dto.AppointmentDto
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface RemoteAppointmentsDataSource {
    fun getPatientAppointments(): Flow<RequestState<List<AppointmentDto>, DataError.Remote>>

    fun getAppointments(): Flow<RequestState<List<AppointmentDto>, DataError.Remote>>

    fun getAppointments(statusId: Short): Flow<RequestState<List<AppointmentDto>, DataError.Remote>>

    fun bookAppointment(
        doctorId: String,
        dateTime: Long,
    ): Flow<RequestState<Unit, DataError.Remote>>

    fun updateAppointment(appointment: AppointmentDto): Flow<RequestState<Unit, DataError.Remote>>

    fun getAdminStatistics(): Flow<RequestState<AdminStatisticsDto, DataError.Remote>>
}
