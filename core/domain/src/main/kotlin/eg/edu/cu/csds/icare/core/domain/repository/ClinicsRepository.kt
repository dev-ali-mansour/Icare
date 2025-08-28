package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Clinician
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

    fun getCurrentDoctor(): Flow<Result<Doctor, DataError.Remote>>

    fun getDoctorSchedule(): Flow<Result<DoctorSchedule, DataError.Remote>>

    fun listTopDoctors(): Flow<Result<List<Doctor>, DataError.Remote>>

    fun listClinicDoctors(clinicId: Long): Flow<Result<List<Doctor>, DataError.Remote>>

    fun addNewDoctor(doctor: Doctor): Flow<Result<Unit, DataError.Remote>>

    fun updateDoctor(doctor: Doctor): Flow<Result<Unit, DataError.Remote>>

    fun listClinicians(): Flow<Result<List<Clinician>, DataError.Remote>>

    fun addNewClinician(clinician: Clinician): Flow<Result<Unit, DataError.Remote>>

    fun updateClinician(clinician: Clinician): Flow<Result<Unit, DataError.Remote>>
}
