package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.AppointmentsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class AppointmentsRepositoryImpl : AppointmentsRepository {
    override fun getPatientAppointments(): Flow<List<Appointment>> {
        TODO("Not yet implemented")
    }

    override fun getAppointments(): Flow<List<Appointment>> {
        TODO("Not yet implemented")
    }

    override fun getAppointments(statusId: Short): Flow<List<Appointment>> {
        TODO("Not yet implemented")
    }

    override fun bookAppointment(
        doctorId: Int,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateAppointmentStatus(
        appointmentId: Long,
        statusId: Short,
    ): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun rescheduleAppointment(
        appointmentId: Long,
        dateTime: Long,
    ): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }
}
