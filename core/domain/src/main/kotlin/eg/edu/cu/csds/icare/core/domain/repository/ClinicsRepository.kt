package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ClinicsRepository {
    fun listClinics(forceUpdate: Boolean): Flow<Result<List<Clinic>, DataError.Remote>>

    fun addNewClinic(clinic: Clinic): Flow<Result<Unit, DataError.Remote>>

    fun updateClinic(clinic: Clinic): Flow<Result<Unit, DataError.Remote>>

    fun listDoctors(forceUpdate: Boolean): Flow<Result<List<Doctor>, DataError.Remote>>

    fun getDoctorSchedule(uid: String): Flow<Result<DoctorSchedule, DataError.Remote>>

    fun listTopDoctors(): Flow<Result<List<Doctor>, DataError.Remote>>

    fun listClinicDoctors(clinicId: Long): Flow<Result<List<Doctor>, DataError.Remote>>

    fun addNewDoctor(doctor: Doctor): Flow<Result<Unit, DataError.Remote>>

    fun getDoctor(doctorId: Long): Flow<Result<Doctor, DataError.Local>>

    fun updateDoctor(doctor: Doctor): Flow<Result<Unit, DataError.Remote>>

    fun listClinicStaff(): Flow<Result<List<ClinicStaff>, DataError.Remote>>

    fun addNewClinicStaff(staff: ClinicStaff): Flow<Result<Unit, DataError.Remote>>

    fun updateClinicStaff(staff: ClinicStaff): Flow<Result<Unit, DataError.Remote>>
}
