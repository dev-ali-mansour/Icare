package eg.edu.cu.csds.icare.data.remote.serivce

import eg.edu.cu.csds.icare.core.domain.model.ActionResultResponse
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.AppointmentsResponse
import eg.edu.cu.csds.icare.core.domain.model.BookingMethodsResponse
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.CenterStaffResponse
import eg.edu.cu.csds.icare.core.domain.model.CentersResponse
import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaffResponse
import eg.edu.cu.csds.icare.core.domain.model.ClinicsResponse
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorsResponse
import eg.edu.cu.csds.icare.core.domain.model.LabImagingCenter
import eg.edu.cu.csds.icare.core.domain.model.LoginInfoResponse
import eg.edu.cu.csds.icare.core.domain.model.PharmaciesResponse
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.PharmacistsResponse
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.RegisteredResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("bookingMethods")
    suspend fun fetchBookingMethods(): Response<BookingMethodsResponse>

    @POST("clinicApi/getClinics")
    suspend fun fetchClinics(
        @Body body: HashMap<String, String>,
    ): Response<ClinicsResponse>

    @POST("clinicApi/getDoctors")
    suspend fun fetchDoctors(
        @Body body: HashMap<String, String>,
    ): Response<DoctorsResponse>

    @POST("imagingCentersApi/getImagingCenters")
    suspend fun fetchCenters(
        @Body body: HashMap<String, String>,
    ): Response<CentersResponse>

    @POST("pharmacyApi/getPharmacy")
    suspend fun fetchPharmacies(
        @Body body: HashMap<String, String>,
    ): Response<PharmaciesResponse>

    @POST("isRegistered")
    suspend fun isRegistered(
        @Body body: HashMap<String, String>,
    ): Response<RegisteredResponse>

    @POST("/userApi/patientRegister")
    suspend fun register(
        @Body body: HashMap<String, String>,
    ): Response<ActionResultResponse>

    @POST("loginInfo")
    suspend fun getLoginInfo(
        @Body body: HashMap<String, String>,
    ): Response<LoginInfoResponse>

    @POST("appointmentApi/patientAppointment")
    suspend fun getPatientAppointments(
        @Body body: HashMap<String, String>,
    ): Response<AppointmentsResponse>

    @POST("appointmentApi/allAppointments")
    suspend fun getAppointments(
        @Body body: HashMap<String, String>,
    ): Response<AppointmentsResponse>

    @POST("appointmentApi/appointmentByStatus")
    suspend fun getAppointmentsByStatus(
        @Body body: HashMap<String, String>,
    ): Response<AppointmentsResponse>

    @POST("appointmentApi/bookAppointment")
    suspend fun bookAppointment(
        @Body body: Appointment,
    ): Response<ActionResultResponse>

    @POST("appointmentApi/bookAppointment")
    suspend fun updateAppointment(
        @Body body: Appointment,
    ): Response<ActionResultResponse>

    @POST("clinicApi/addClinic")
    suspend fun addNewClinic(
        @Body body: Clinic,
    ): Response<ActionResultResponse>

    @POST("clinicApi/addClinic")
    suspend fun updateClinic(
        @Body body: Clinic,
    ): Response<ActionResultResponse>

    @POST("userApi/doctorRegister")
    suspend fun addNewDoctor(
        @Body body: Doctor,
    ): Response<ActionResultResponse>

    @POST("userApi/doctor_register")
    suspend fun updateDoctor(
        @Body body: Doctor,
    ): Response<ActionResultResponse>

    @POST("listClinicStaff")
    suspend fun listClinicStaff(
        @Body body: HashMap<String, String>,
    ): Response<ClinicStaffResponse>

    @POST("addNewClinicStaff")
    suspend fun addNewClinicStaff(
        @Body body: ClinicStaff,
    ): Response<ActionResultResponse>

    @POST("updateClinicStaff")
    suspend fun updateClinicStaff(
        @Body body: ClinicStaff,
    ): Response<ActionResultResponse>

    @POST("pharmacyApi/addPharmacy")
    suspend fun addNewPharmacy(
        @Body body: Pharmacy,
    ): Response<ActionResultResponse>

    @POST("pharmacyApi/addPharmacy")
    suspend fun updatePharmacy(
        @Body body: Pharmacy,
    ): Response<ActionResultResponse>

    @POST("pharmacyApi/getPharmaciest")
    suspend fun listPharmacists(
        @Body body: HashMap<String, String>,
    ): Response<PharmacistsResponse>

    @POST("userApi/pharmacistRegister")
    suspend fun addNewPharmacist(
        @Body body: Pharmacist,
    ): Response<ActionResultResponse>

    @POST("userApi/pharmacistRegister")
    suspend fun updatePharmacist(
        @Body body: Pharmacist,
    ): Response<ActionResultResponse>

    @POST("imagingCentersApi/addImagingCenter")
    suspend fun addNewCenter(
        @Body body: LabImagingCenter,
    ): Response<ActionResultResponse>

    @POST("imagingCentersApi/addImagingCenter")
    suspend fun updateCenter(
        @Body body: LabImagingCenter,
    ): Response<ActionResultResponse>

    @POST("listCenterStaff")
    suspend fun listCenterStaff(
        @Body body: HashMap<String, String>,
    ): Response<CenterStaffResponse>

    @POST("userApi/centerStaffRegister")
    suspend fun addNewCenterStaff(
        @Body body: CenterStaff,
    ): Response<ActionResultResponse>

    @POST("userApi/centerStaffRegister")
    suspend fun updateCenterStaff(
        @Body body: CenterStaff,
    ): Response<ActionResultResponse>
}
