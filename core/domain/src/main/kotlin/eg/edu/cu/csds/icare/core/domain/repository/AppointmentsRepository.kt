package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface AppointmentsRepository {
    fun getPatientAppointments(): Flow<Result<List<Appointment>, DataError.Remote>>

    fun getAppointments(): Flow<Result<List<Appointment>, DataError.Remote>>

    fun getAppointments(statusId: Short): Flow<Result<List<Appointment>, DataError.Remote>>

    fun bookAppointment(appointment: Appointment): Flow<Result<Unit, DataError.Remote>>

    fun updateAppointment(appointment: Appointment): Flow<Result<Unit, DataError.Remote>>

    fun getAdminStatistics(): Flow<Result<AdminStatistics, DataError.Remote>>
}
