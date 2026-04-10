package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.AdminStatistics
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface AppointmentsRepository {
    fun getPatientAppointments(): Flow<RequestState<List<Appointment>, DataError.Remote>>

    fun getAppointments(): Flow<RequestState<List<Appointment>, DataError.Remote>>

    fun getAppointments(statusId: Short): Flow<RequestState<List<Appointment>, DataError.Remote>>

    fun bookAppointment(
        doctorId: String,
        dateTime: Long,
    ): Flow<RequestState<Unit, DataError.Remote>>

    fun updateAppointment(appointment: Appointment): Flow<RequestState<Unit, DataError.Remote>>

    fun getAdminStatistics(): Flow<RequestState<AdminStatistics, DataError.Remote>>
}
