package eg.edu.cu.csds.icare.data.remote.datasource

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteClinicsDataSource {
    fun fetchClinics(): Flow<Resource<List<Clinic>>>

    fun addNewClinic(clinic: Clinic): Flow<Resource<Nothing?>>

    fun updateClinic(clinic: Clinic): Flow<Resource<Nothing?>>

    fun fetchDoctors(): Flow<Resource<List<Doctor>>>

    fun addNewDoctor(doctor: Doctor): Flow<Resource<Nothing?>>

    fun updateDoctor(doctor: Doctor): Flow<Resource<Nothing?>>

    fun getDoctorSchedule(uid: String): Flow<Resource<DoctorSchedule>>

    fun listClinicStaff(clinicId: Long): Flow<Resource<List<ClinicStaff>>>

    fun addNewClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>>

    fun updateClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>>
}
