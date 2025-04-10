package eg.edu.cu.csds.icare.data.repository

import eg.edu.cu.csds.icare.core.domain.model.Clinic
import eg.edu.cu.csds.icare.core.domain.model.ClinicStaff
import eg.edu.cu.csds.icare.core.domain.model.Doctor
import eg.edu.cu.csds.icare.core.domain.model.Resource
import eg.edu.cu.csds.icare.core.domain.repository.ClinicsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class ClinicsRepositoryImpl : ClinicsRepository {
    override fun listClinics(forceUpdate: Boolean): Flow<Resource<List<Clinic>>> {
        TODO("Not yet implemented")
    }

    override fun addNewClinic(clinic: Clinic): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateClinic(clinic: Clinic): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun listDoctors(forceUpdate: Boolean): Flow<Resource<List<Doctor>>> {
        TODO("Not yet implemented")
    }

    override fun listClinicDoctors(clinicId: Long): Flow<Resource<List<Doctor>>> {
        TODO("Not yet implemented")
    }

    override fun addNewDoctor(doctor: Doctor): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateDoctor(doctor: Doctor): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun listClinicStaff(clinicId: Long): Flow<Resource<List<ClinicStaff>>> {
        TODO("Not yet implemented")
    }

    override fun addNewClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }

    override fun updateClinicStaff(staff: ClinicStaff): Flow<Resource<Nothing?>> {
        TODO("Not yet implemented")
    }
}
