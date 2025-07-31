package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.ClinicDto
import eg.edu.cu.csds.icare.core.data.dto.ClinicStaffDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorScheduleDto
import eg.edu.cu.csds.icare.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteClinicsDataSource {
    fun fetchClinics(): Flow<Resource<List<ClinicDto>>>

    fun addNewClinic(clinic: ClinicDto): Flow<Resource<Nothing?>>

    fun updateClinic(clinic: ClinicDto): Flow<Resource<Nothing?>>

    fun fetchDoctors(): Flow<Resource<List<DoctorDto>>>

    fun addNewDoctor(doctor: DoctorDto): Flow<Resource<Nothing?>>

    fun updateDoctor(doctor: DoctorDto): Flow<Resource<Nothing?>>

    fun getDoctorSchedule(uid: String): Flow<Resource<DoctorScheduleDto>>

    fun listClinicStaff(): Flow<Resource<List<ClinicStaffDto>>>

    fun addNewClinicStaff(staff: ClinicStaffDto): Flow<Resource<Nothing?>>

    fun updateClinicStaff(staff: ClinicStaffDto): Flow<Resource<Nothing?>>
}
