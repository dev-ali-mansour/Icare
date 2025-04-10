package eg.edu.cu.csds.icare.data.remote.serivce

import eg.edu.cu.csds.icare.core.domain.model.ActionResultResponse
import eg.edu.cu.csds.icare.core.domain.model.AppointmentsResponse
import eg.edu.cu.csds.icare.core.domain.model.BookingMethodsResponse
import eg.edu.cu.csds.icare.core.domain.model.CentersResponse
import eg.edu.cu.csds.icare.core.domain.model.ClinicsResponse
import eg.edu.cu.csds.icare.core.domain.model.DoctorsResponse
import eg.edu.cu.csds.icare.core.domain.model.LoginInfoResponse
import eg.edu.cu.csds.icare.core.domain.model.PharmaciesResponse
import eg.edu.cu.csds.icare.core.domain.model.RegisteredResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("bookingMethods")
    suspend fun fetchBookingMethods(): Response<BookingMethodsResponse>

    @POST("clinics")
    suspend fun fetchClinics(): Response<ClinicsResponse>

    @POST("doctors")
    suspend fun fetchDoctors(): Response<DoctorsResponse>

    @POST("centers")
    suspend fun fetchCenters(): Response<CentersResponse>

    @POST("pharmacies")
    suspend fun fetchPharmacies(): Response<PharmaciesResponse>

    @POST("isRegistered")
    suspend fun isRegistered(
        @Body body: HashMap<String, String>,
    ): Response<RegisteredResponse>

    @POST("patient_register")
    suspend fun register(
        @Body body: HashMap<String, String>,
    ): Response<LoginInfoResponse>

    @POST("loginInfo")
    suspend fun getLoginInfo(): Response<LoginInfoResponse>

    @POST("patientAppointments")
    suspend fun getPatientAppointments(): Response<AppointmentsResponse>

    @POST("allAppointments")
    suspend fun getAppointments(): Response<AppointmentsResponse>

    @POST("appointmentsByStatus")
    suspend fun getAppointmentsByStatus(
        @Body body: HashMap<String, String>,
    ): Response<AppointmentsResponse>

    @POST("bookAppointment")
    suspend fun bookAppointment(
        @Body body: HashMap<String, String>,
    ): Response<ActionResultResponse>

    @POST("updateAppointmentStatus")
    suspend fun updateAppointmentStatus(
        @Body body: HashMap<String, String>,
    ): Response<ActionResultResponse>

    @POST("rescheduleAppointment")
    suspend fun rescheduleAppointment(
        @Body body: HashMap<String, String>,
    ): Response<ActionResultResponse>
}
