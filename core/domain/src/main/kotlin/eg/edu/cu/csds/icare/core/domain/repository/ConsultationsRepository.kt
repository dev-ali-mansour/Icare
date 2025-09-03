package eg.edu.cu.csds.icare.core.domain.repository

import eg.edu.cu.csds.icare.core.domain.model.Consultation
import eg.edu.cu.csds.icare.core.domain.model.DataError
import eg.edu.cu.csds.icare.core.domain.model.MedicalRecord
import eg.edu.cu.csds.icare.core.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ConsultationsRepository {
    fun addNewConsultation(consultation: Consultation): Flow<Result<Unit, DataError.Remote>>

    fun updateConsultation(consultation: Consultation): Flow<Result<Unit, DataError.Remote>>

    fun getMedicalRecord(patientId: String): Flow<Result<MedicalRecord, DataError.Remote>>

    fun getMedicationsByStatus(statusId: Short): Flow<Result<List<Consultation>, DataError.Remote>>

    fun getLabTestsByStatus(statusId: Short): Flow<Result<List<Consultation>, DataError.Remote>>

    fun getImagingTestsByStatus(statusId: Short): Flow<Result<List<Consultation>, DataError.Remote>>
}
