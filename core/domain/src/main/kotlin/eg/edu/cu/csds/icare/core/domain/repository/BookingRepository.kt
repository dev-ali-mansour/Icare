package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface BookingRepository {

    fun getAppointments(patientId: String): Flow<List<Appointment>>

    suspend fun bookAppointment(appointment: Appointment): Result<Unit>

    suspend fun cancelAppointment(appointmentId: String): Result<Unit>

    suspend fun rescheduleAppointment(
        appointmentId: String,
        newDate: String,
        newTime: String
    ): Result<Unit>



}