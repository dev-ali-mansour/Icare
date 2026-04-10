package eg.edu.cu.csds.icare.core.data.remote.datasource

import eg.edu.cu.csds.icare.core.data.dto.ClinicDto
import eg.edu.cu.csds.icare.core.data.dto.ClinicianDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorDto
import eg.edu.cu.csds.icare.core.data.dto.DoctorScheduleDto
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface RemoteClinicsDataSource {
    fun fetchClinics(): Flow<RequestState<List<ClinicDto>, DataError.Remote>>

    fun addNewClinic(clinic: ClinicDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updateClinic(clinic: ClinicDto): Flow<RequestState<Unit, DataError.Remote>>

    fun fetchDoctors(): Flow<RequestState<List<DoctorDto>, DataError.Remote>>

    fun addNewDoctor(doctor: DoctorDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updateDoctor(doctor: DoctorDto): Flow<RequestState<Unit, DataError.Remote>>

    fun getDoctorSchedule(uid: String?): Flow<RequestState<DoctorScheduleDto, DataError.Remote>>

    fun listClinicians(): Flow<RequestState<List<ClinicianDto>, DataError.Remote>>

    fun addNewClinician(clinician: ClinicianDto): Flow<RequestState<Unit, DataError.Remote>>

    fun updateClinician(clinician: ClinicianDto): Flow<RequestState<Unit, DataError.Remote>>
}
