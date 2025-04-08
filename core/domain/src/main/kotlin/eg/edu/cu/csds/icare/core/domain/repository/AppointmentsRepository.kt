package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface AppointmentsRepository {
    fun getPatientAppointments(): Flow<Resource<List<Appointment>>>

    fun getAppointments(): Flow<Resource<List<Appointment>>>

    fun getAppointments(statusId: Short): Flow<Resource<List<Appointment>>>

    fun bookAppointment(
        doctorId: Int,
        dateTime: Long,
    ): Flow<Resource<Nothing?>>

    fun updateAppointmentStatus(
        appointmentId: Long,
        statusId: Short,
    ): Flow<Resource<Nothing?>>

    fun rescheduleAppointment(
        appointmentId: Long,
        dateTime: Long,
    ): Flow<Resource<Nothing?>>
}
