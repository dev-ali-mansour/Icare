package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.Clinician
import eg.edu.cu.csds.icare.core.domain.util.DataError
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.DoctorSchedule
import eg.edu.cu.csds.icare.core.domain.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ClinicsRepository {
    fun listClinics(forceUpdate: Boolean): Flow<RequestState<List<Clinic>, DataError.Remote>>

    fun addNewClinic(clinic: Clinic): Flow<RequestState<Unit, DataError.Remote>>

    fun updateClinic(clinic: Clinic): Flow<RequestState<Unit, DataError.Remote>>

    fun listDoctors(forceUpdate: Boolean): Flow<RequestState<List<Doctor>, DataError.Remote>>

    fun getCurrentDoctor(): Flow<RequestState<Doctor, DataError.Remote>>

    fun getDoctorSchedule(uid: String?): Flow<RequestState<DoctorSchedule, DataError.Remote>>

    fun listTopDoctors(): Flow<RequestState<List<Doctor>, DataError.Remote>>

    fun listClinicDoctors(clinicId: Long): Flow<RequestState<List<Doctor>, DataError.Remote>>

    fun addNewDoctor(doctor: Doctor): Flow<RequestState<Unit, DataError.Remote>>

    fun updateDoctor(doctor: Doctor): Flow<RequestState<Unit, DataError.Remote>>

    fun listClinicians(): Flow<RequestState<List<Clinician>, DataError.Remote>>

    fun addNewClinician(clinician: Clinician): Flow<RequestState<Unit, DataError.Remote>>

    fun updateClinician(clinician: Clinician): Flow<RequestState<Unit, DataError.Remote>>
}
