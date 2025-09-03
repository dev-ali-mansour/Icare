package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.AdminStatisticsDto
import eg.edu.cu.csds.icare.core.data.dto.AppointmentDto
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface RemoteAppointmentsDataSource {
    fun getPatientAppointments(): Flow<Result<List<AppointmentDto>, DataError.Remote>>

    fun getAppointments(): Flow<Result<List<AppointmentDto>, DataError.Remote>>

    fun getAppointments(statusId: Short): Flow<Result<List<AppointmentDto>, DataError.Remote>>

    fun bookAppointment(
        doctorId: String,
        dateTime: Long,
    ): Flow<Result<Unit, DataError.Remote>>

    fun updateAppointment(appointment: AppointmentDto): Flow<Result<Unit, DataError.Remote>>

    fun getAdminStatistics(): Flow<Result<AdminStatisticsDto, DataError.Remote>>
}
