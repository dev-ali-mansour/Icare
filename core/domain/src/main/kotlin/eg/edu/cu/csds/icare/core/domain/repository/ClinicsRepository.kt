package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface ClinicsRepository {
    fun listClinics(forceRefresh: Boolean): Flow<Resource<List<Clinic>>>

    fun addNewClinic(clinic: Clinic): Flow<Resource<Nothing?>>

    fun updateClinic(clinic: Clinic): Flow<Resource<Nothing?>>

    fun listDoctors(forceRefresh: Boolean): Flow<Resource<List<Doctor>>>

    fun getDoctorSchedule(uid: String): Flow<Resource<DoctorSchedule>>

    fun listTopDoctors(): Flow<Resource<List<Doctor>>>

    fun listClinicDoctors(clinicId: Long): Flow<Resource<List<Doctor>>>

    fun addNewDoctor(doctor: Doctor): Flow<Resource<Nothing?>>

    fun updateDoctor(doctor: Doctor): Flow<Resource<Nothing?>>

    fun listClinicStaff(clinicId: Long): Flow<Resource<List<ClinicStaff>>>

    fun addNewClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>>

    fun updateClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>>
}
