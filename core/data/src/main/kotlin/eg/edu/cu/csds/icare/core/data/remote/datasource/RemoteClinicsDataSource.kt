package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.ClinicDto
import eg.edu.cu.csds.icare.core.data.dto.ClinicianDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorScheduleDto
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface RemoteClinicsDataSource {
    fun fetchClinics(): Flow<Result<List<ClinicDto>, DataError.Remote>>

    fun addNewClinic(clinic: ClinicDto): Flow<Result<Unit, DataError.Remote>>

    fun updateClinic(clinic: ClinicDto): Flow<Result<Unit, DataError.Remote>>

    fun fetchDoctors(): Flow<Result<List<DoctorDto>, DataError.Remote>>

    fun addNewDoctor(doctor: DoctorDto): Flow<Result<Unit, DataError.Remote>>

    fun updateDoctor(doctor: DoctorDto): Flow<Result<Unit, DataError.Remote>>

    fun getDoctorSchedule(uid: String): Flow<Result<DoctorScheduleDto, DataError.Remote>>

    fun listClinicians(): Flow<Result<List<ClinicianDto>, DataError.Remote>>

    fun addNewClinician(clinician: ClinicianDto): Flow<Result<Unit, DataError.Remote>>

    fun updateClinician(clinician: ClinicianDto): Flow<Result<Unit, DataError.Remote>>
}
