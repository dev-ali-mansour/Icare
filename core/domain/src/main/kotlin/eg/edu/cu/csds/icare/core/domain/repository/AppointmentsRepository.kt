package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.BookingMethod
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface AppointmentsRepository {
    fun listBookingMethods(forceRefresh: Boolean = false): Flow<Resource<List<BookingMethod>>>

    fun getPatientAppointments(): Flow<Resource<List<Appointment>>>

    fun getAppointments(): Flow<Resource<List<Appointment>>>

    fun getAppointments(statusId: Short): Flow<Resource<List<Appointment>>>

    fun bookAppointment(appointment: Appointment): Flow<Resource<Nothing?>>

    fun updateAppointment(appointment: Appointment): Flow<Resource<Nothing?>>
}
