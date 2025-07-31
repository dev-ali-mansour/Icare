package eg.edu.cu.csds.icare.core.data.remote.serivce

import eg.edu.cu.csds.icare.core.data.dto.ActionResultResponse
import eg.edu.cu.csds.icare.core.data.dto.CenterDto
import eg.edu.cu.csds.icare.core.data.dto.CentersResponse
import eg.edu.cu.csds.icare.core.data.dto.ClinicDto
import eg.edu.cu.csds.icare.core.data.dto.ClinicStaffDto
import eg.edu.cu.csds.icare.core.data.dto.ClinicStaffResponse
import eg.edu.cu.csds.icare.core.data.dto.ClinicsResponse
import eg.edu.cu.csds.icare.core.data.dto.DoctorDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorsResponse
import eg.edu.cu.csds.icare.core.domain.model.AdminStatisticsResponse
import eg.edu.cu.csds.icare.core.domain.model.Appointment
import eg.edu.cu.csds.icare.core.domain.model.AppointmentsResponse
import eg.edu.cu.csds.icare.core.domain.model.CenterStaff
import eg.edu.cu.csds.icare.core.domain.model.CenterStaffResponse
import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.ConsultationsResponse
import eg.edu.cu.csds.icare.core.domain.model.DoctorScheduleResponse
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecordResponse
import eg.edu.cu.csds.icare.core.domain.model.PharmaciesResponse
import eg.edu.cu.csds.icare.core.domain.model.Pharmacist
import eg.edu.cu.csds.icare.core.domain.model.PharmacistsResponse
import eg.edu.cu.csds.icare.core.domain.model.Pharmacy
import eg.edu.cu.csds.icare.core.domain.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/userApi/loginInfo")
    suspend fun getLoginInfo(
        @Body body: HashMap<String, String>,
    ): Response<UserResponse>

    @POST("/userApi/patientRegister")
    suspend fun register(
        @Body body: HashMap<String, String>,
    ): Response<ActionResultResponse>

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

    @POST("appointmentApi/bookAppointment")
    suspend fun bookAppointment(
        @Body body: Appointment,
    ): Response<ActionResultResponse>

    @POST("appointmentApi/bookAppointment")
    suspend fun updateAppointment(
        @Body body: Appointment,
    ): Response<ActionResultResponse>

    @POST("appointmentApi/patientAppointment")
    suspend fun getPatientAppointments(
        @Body body: HashMap<String, String>,
    ): Response<AppointmentsResponse>

    @POST("appointmentApi/getAppointments")
    suspend fun getAppointments(
        @Body body: HashMap<String, String>,
    ): Response<AppointmentsResponse>

    @POST("appointmentApi/appointmentByStatus")
    suspend fun getAppointmentsByStatus(
        @Body body: HashMap<String, String>,
    ): Response<AppointmentsResponse>

    @POST("clinicApi/addClinic")
    suspend fun upsertClinic(
        @Body body: ClinicDto,
    ): Response<ActionResultResponse>

    @POST("userApi/doctorRegister")
    suspend fun upsertDoctor(
        @Body body: DoctorDto,
    ): Response<ActionResultResponse>

    @POST("clinicApi/doctorSchedule")
    suspend fun getDoctorSchedule(
        @Body body: HashMap<String, String>,
    ): Response<DoctorScheduleResponse>

    @POST("userApi/getClinicStaff")
    suspend fun listClinicStaff(
        @Body body: HashMap<String, String>,
    ): Response<ClinicStaffResponse>

    @POST("userApi/clinicStaffRegister")
    suspend fun upsertClinicStaff(
        @Body body: ClinicStaffDto,
    ): Response<ActionResultResponse>

    @POST("pharmacyApi/addPharmacy")
    suspend fun upsertPharmacy(
        @Body body: Pharmacy,
    ): Response<ActionResultResponse>

    @POST("userApi/getPharmacists")
    suspend fun listPharmacists(
        @Body body: HashMap<String, String>,
    ): Response<PharmacistsResponse>

    @POST("userApi/pharmacistRegister")
    suspend fun upsertPharmacist(
        @Body body: Pharmacist,
    ): Response<ActionResultResponse>

    @POST("imagingCentersApi/addImagingCenter")
    suspend fun upsertCenter(
        @Body body: CenterDto,
    ): Response<ActionResultResponse>

    @POST("userApi/getCenterStaff")
    suspend fun listCenterStaff(
        @Body body: HashMap<String, String>,
    ): Response<CenterStaffResponse>

    @POST("userApi/centerStaffRegister")
    suspend fun upsertCenterStaff(
        @Body body: CenterStaff,
    ): Response<ActionResultResponse>

    @POST("clinicApi/consultation")
    suspend fun upsertConsultation(
        @Body body: Consultation,
    ): Response<ActionResultResponse>

    @POST("clinicApi/medicalRecord")
    suspend fun getMedicalRecord(
        @Body body: HashMap<String, String>,
    ): Response<MedicalRecordResponse>

    @POST("clinicApi/getConsultationsByPrescriptionStatus")
    suspend fun getMedicationsByStatus(
        @Body body: HashMap<String, String>,
    ): Response<ConsultationsResponse>

    @POST("clinicApi/getConsultationsByLabTestStatus")
    suspend fun getLabTestsByStatus(
        @Body body: HashMap<String, String>,
    ): Response<ConsultationsResponse>

    @POST("clinicApi/getConsultationsByImaginingTestStatus")
    suspend fun getImagingTestsByStatus(
        @Body body: HashMap<String, String>,
    ): Response<ConsultationsResponse>

    @POST("clinicApi/getAdminStatistics")
    suspend fun getAdminStatistics(
        @Body body: HashMap<String, String>,
    ): Response<AdminStatisticsResponse>
}
